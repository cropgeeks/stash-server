package jhi.seedstore.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.*;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.enums.UsersUserType;
import jhi.seedstore.database.codegen.tables.pojos.ContainerTypes;
import jhi.seedstore.database.codegen.tables.records.ContainerTypesRecord;
import jhi.seedstore.pojo.*;
import jhi.seedstore.resource.base.BaseResource;
import jhi.seedstore.util.*;
import org.jooq.*;
import org.jooq.Record;

import java.sql.*;
import java.util.List;

import static jhi.seedstore.database.codegen.tables.ContainerTypes.*;

@Path("containertype")
public class ContainerTypeResource extends BaseResource
{
	@GET
	@Path("/{containerTypeId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured
	@PermitAll
	public ContainerTypes getContainer(@PathParam("containerTypeId") Integer containerTypeId)
		throws SQLException
	{
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectWhereStep<ContainerTypesRecord> from = context.selectFrom(CONTAINER_TYPES);

			if (containerTypeId != null)
				from.where(CONTAINER_TYPES.ID.eq(containerTypeId));

			return from.fetchAnyInto(ContainerTypes.class);
		}
	}

	@DELETE
	@Path("/{containerTypeId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.admin)
	public Response deleteContainer(@PathParam("containerTypeId") Integer containerTypeId)
			throws SQLException
	{
		if (containerTypeId == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			return Response.ok(context.deleteFrom(CONTAINER_TYPES).where(CONTAINER_TYPES.ID.eq(containerTypeId)).execute() > 0).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.admin)
	public Response postContainer(ContainerTypes containerType)
			throws SQLException
	{
		if (containerType == null || StringUtils.isEmpty(containerType.getName()))
			return Response.status(Response.Status.BAD_REQUEST).build();
		if (containerType.getName().length() > 255)
			return Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			// Create new entry
			ContainerTypesRecord record = context.newRecord(CONTAINER_TYPES, containerType);
			record.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			record.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
			record.store();

			// Return the id
			return Response.ok(record.into(ContainerTypes.class)).build();
		}
	}

	@POST
	@Path("/table")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured
	@PermitAll
	public PaginatedResult<List<ContainerTypes>> postContainerTable(PaginatedRequest request)
		throws SQLException
	{
		processRequest(request);
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectSelectStep<Record> select = context.select();

			if (previousCount == -1)
				select.hint("SQL_CALC_FOUND_ROWS");

			SelectJoinStep<Record> from = select.from(CONTAINER_TYPES);

			// Filter here!
			where(from, filters);

			List<ContainerTypes> result = setPaginationAndOrderBy(from)
				.fetch()
				.into(ContainerTypes.class);

			long count = previousCount == -1 ? context.fetchOne("SELECT FOUND_ROWS()").into(Long.class) : previousCount;

			return new PaginatedResult<>(result, count);
		}
	}
}
