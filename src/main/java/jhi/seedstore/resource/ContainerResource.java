package jhi.seedstore.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.tables.pojos.*;
import jhi.seedstore.database.codegen.tables.records.*;
import jhi.seedstore.pojo.*;
import jhi.seedstore.resource.base.BaseResource;
import jhi.seedstore.util.*;
import org.jooq.*;

import java.sql.*;
import java.util.List;

import static jhi.seedstore.database.codegen.tables.ContainerTypes.*;
import static jhi.seedstore.database.codegen.tables.Containers.*;
import static jhi.seedstore.database.codegen.tables.Projects.*;
import static jhi.seedstore.database.codegen.tables.Trials.*;
import static jhi.seedstore.database.codegen.tables.ViewTableContainers.*;

@Path("container")
@Secured
@PermitAll
public class ContainerResource extends BaseResource
{
	@GET
	@Path("/{containerId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ViewTableContainers getContainer(@PathParam("containerId") Integer containerId)
		throws SQLException
	{
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectWhereStep<ViewTableContainersRecord> from = context.selectFrom(VIEW_TABLE_CONTAINERS);

			if (containerId != null)
				from.where(VIEW_TABLE_CONTAINERS.CONTAINER_ID.eq(containerId));

			return from.fetchAnyInto(ViewTableContainers.class);
		}
	}

	@DELETE
	@Path("/{containerId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteContainer(@PathParam("containerId") Integer containerId)
		throws SQLException
	{
		if (containerId == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			return Response.ok(context.deleteFrom(CONTAINERS).where(CONTAINERS.ID.eq(containerId)).execute() > 0).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postContainer(Containers container)
		throws SQLException
	{
		// Payload empty or required field empty
		if (container == null || StringUtils.isAnyEmpty(container.getBarcode(), container.getDescription()) || container.getContainerTypeId() == null || container.getIsActive() == null || container.getProjectId() == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		// Barcode or description too long
		if (container.getBarcode().length() > 100 || container.getDescription().length() > 255)
			return Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			// Check referenced entities
			boolean typeExists = context.selectFrom(CONTAINER_TYPES).where(CONTAINER_TYPES.ID.eq(container.getContainerTypeId())).fetchAny() != null;
			boolean projectExists = context.selectFrom(PROJECTS).where(PROJECTS.ID.eq(container.getProjectId())).fetchAny() != null;
			boolean trialExists = container.getTrialId() == null || context.selectFrom(TRIALS).where(TRIALS.ID.eq(container.getTrialId())).fetchAny() != null;
			boolean parentExists = container.getParentContainerId() == null || context.selectFrom(CONTAINERS).where(CONTAINERS.ID.eq(container.getParentContainerId())).fetchAny() != null;

			if (!typeExists)
				return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Container type not found.").build();
			if (!projectExists)
				return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Project not found.").build();
			if (!trialExists)
				return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Trial not found.").build();
			if (!parentExists)
				return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Parent container not found.").build();

			// Create new entry
			ContainersRecord record = context.newRecord(CONTAINERS, container);
			record.store();

			// Return the id
			return Response.ok(record.getId()).build();
		}
	}

	@POST
	@Path("/table")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PaginatedResult<List<ViewTableContainers>> postContainerTable(PaginatedRequest request)
		throws SQLException
	{
		processRequest(request);
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectSelectStep<Record> select = context.select();

			if (previousCount == -1)
				select.hint("SQL_CALC_FOUND_ROWS");

			SelectJoinStep<Record> from = select.from(VIEW_TABLE_CONTAINERS);

			// Filter here!
			filter(from, filters);

			List<ViewTableContainers> result = setPaginationAndOrderBy(from)
				.fetch()
				.into(ViewTableContainers.class);

			long count = previousCount == -1 ? context.fetchOne("SELECT FOUND_ROWS()").into(Long.class) : previousCount;

			return new PaginatedResult<>(result, count);
		}
	}
}
