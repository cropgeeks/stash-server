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
import org.jooq.impl.DSL;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.stream.Collectors;

import static jhi.seedstore.database.codegen.tables.Attributes.*;
import static jhi.seedstore.database.codegen.tables.ContainerAttributes.*;
import static jhi.seedstore.database.codegen.tables.ContainerTypes.*;
import static jhi.seedstore.database.codegen.tables.Containers.*;
import static jhi.seedstore.database.codegen.tables.Projects.*;
import static jhi.seedstore.database.codegen.tables.TransferLogs.*;
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

	@GET
	@Path("/{containerId:\\d+}/clear")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response emptyContainer(@PathParam("containerId") Integer containerId)
		throws SQLException
	{
		if (containerId == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		AuthenticationFilter.UserDetails sessionUser = (AuthenticationFilter.UserDetails) securityContext.getUserPrincipal();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			// Add transfer log entries to indicate they've been removed from their parent
			context.insertInto(TRANSFER_LOGS, TRANSFER_LOGS.CONTAINER_ID, TRANSFER_LOGS.SOURCE_ID, TRANSFER_LOGS.TARGET_ID, TRANSFER_LOGS.USER_ID)
				   .select(DSL.select(CONTAINERS.ID, CONTAINERS.PARENT_CONTAINER_ID, DSL.inline(null, Integer.class), DSL.inline(sessionUser.getId(), Integer.class))
							  .from(CONTAINERS)
							  .where(CONTAINERS.PARENT_CONTAINER_ID.eq(containerId))
				   )
				   .execute();

			// Set them to inactive
			context.update(CONTAINERS)
				   .set(CONTAINERS.IS_ACTIVE, false)
				   .setNull(CONTAINERS.PARENT_CONTAINER_ID)
				   .where(CONTAINERS.PARENT_CONTAINER_ID.eq(containerId)).execute();

			return Response.ok().build();
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
	public Response postContainer(List<ViewTableContainers> containers)
		throws SQLException
	{
		if (CollectionUtils.isEmpty(containers))
			return Response.status(Response.Status.BAD_REQUEST).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			for (ViewTableContainers container : containers)
			{
				// Payload empty or required field empty
				if (container == null || StringUtils.isEmpty(container.getContainerBarcode()) || container.getContainerTypeId() == null || container.getContainerIsActive() == null)
					return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "Either the container is null or the barcode or description aren't set or the container type or isActive state aren't set.").build();

				// Barcode or description too long
				if (container.getContainerBarcode().length() > 100 || (container.getContainerDescription() != null && container.getContainerDescription().length() > 255))
					return Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE).build();

				List<String> barcodes = containers.stream().map(ViewTableContainers::getContainerBarcode).collect(Collectors.toList());

				int count = context.fetchCount(context.selectFrom(CONTAINERS).where(CONTAINERS.BARCODE.in(barcodes)));

				if (count > 0)
					return Response.status(Response.Status.CONFLICT).build();
			}

			Map<String, Attributes> attributes = new HashMap<>();
			Set<Integer> typeIds = new HashSet<>();
			Set<Integer> projectIds = new HashSet<>();
			Set<Integer> trialIds = new HashSet<>();
			Set<Integer> parentIds = new HashSet<>();
			Set<String> attributeNames = new HashSet<>();
			for (ViewTableContainers c : containers)
			{
				if (c.getContainerTypeId() != null) typeIds.add(c.getContainerTypeId());
				if (c.getProjectId() != null) projectIds.add(c.getProjectId());
				if (c.getTrialId() != null) trialIds.add(c.getTrialId());
				if (c.getParentId() != null) parentIds.add(c.getParentId());
				if (!CollectionUtils.isEmpty(c.getContainerAttributes()))
				{
					for (ContainerAttributeValue a : c.getContainerAttributes())
					{
						if (a.getAttributeName() == null || StringUtils.isEmpty(a.getAttributeValue()))
							return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), "The attribute name or value of a specified attribute mapping is empty.").build();
						else
						{
							Attributes attr = attributes.get(a.getAttributeName());
							if (attr == null)
							{
								attr = context.selectFrom(ATTRIBUTES).where(ATTRIBUTES.NAME.eq(a.getAttributeName())).fetchAnyInto(Attributes.class);

								if (attr == null)
									return Response.status(Response.Status.NOT_FOUND).build();
								else
									attributes.put(attr.getName(), attr);
							}

							switch (attr.getDatatype())
							{
								case numeric:
									try
									{
										Double.parseDouble(a.getAttributeValue());
									}
									catch (NumberFormatException e)
									{
										return Response.status(Response.Status.EXPECTATION_FAILED.getStatusCode(), a.getAttributeValue()).build();
									}
									break;
								case date:
									try
									{
										sdf.parse(a.getAttributeValue());
									}
									catch (ParseException e)
									{
										return Response.status(Response.Status.EXPECTATION_FAILED.getStatusCode(), a.getAttributeValue()).build();
									}
									break;
							}

							attributeNames.add(a.getAttributeName());
						}
					}
				}
			}

			// Check referenced entities
			boolean typeExists = context.fetchCount(context.selectFrom(CONTAINER_TYPES).where(CONTAINER_TYPES.ID.in(typeIds))) == typeIds.size();
			boolean projectExists = context.fetchCount(context.selectFrom(PROJECTS).where(PROJECTS.ID.in(projectIds))) == projectIds.size();
			boolean trialExists = context.fetchCount(context.selectFrom(TRIALS).where(TRIALS.ID.in(trialIds))) == trialIds.size();
			boolean parentExists = context.fetchCount(context.selectFrom(CONTAINERS).where(CONTAINERS.ID.in(parentIds))) == parentIds.size();
			boolean attributeExists = context.fetchCount(context.selectFrom(ATTRIBUTES).where(ATTRIBUTES.NAME.in(attributeNames))) == attributeNames.size();

			if (!typeExists) return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Container type not found.").build();
			if (!projectExists) return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Project not found.").build();
			if (!trialExists) return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Trial not found.").build();
			if (!parentExists) return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Parent container not found.").build();
			if (!attributeExists) return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Referenced attribute not found.").build();

			// Create new entries
			List<Integer> ids = new ArrayList<>();
			for (ViewTableContainers container : containers)
			{
				ContainersRecord c = context.newRecord(CONTAINERS);
				c.setParentContainerId(container.getParentId());
				c.setContainerTypeId(container.getContainerTypeId());
				c.setTrialId(container.getTrialId());
				c.setProjectId(container.getProjectId());
				c.setIsActive(container.getContainerIsActive());
				c.setDescription(container.getContainerDescription());
				c.setBarcode(container.getContainerBarcode());
				c.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				c.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
				c.store();
				ids.add(c.getId());

				if (!CollectionUtils.isEmpty(container.getContainerAttributes()))
				{
					for (ContainerAttributeValue attributeValue : container.getContainerAttributes())
					{
						ContainerAttributesRecord a = context.newRecord(CONTAINER_ATTRIBUTES);
						a.setAttributeId(attributes.get(attributeValue.getAttributeName()).getId());
						a.setContainerId(c.getId());
						a.setAttributeValue(attributeValue.getAttributeValue());
						a.store();
					}
				}
			}

			// Return the id
			return Response.ok(ids).build();
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

			if (previousCount == -1) select.hint("SQL_CALC_FOUND_ROWS");

			SelectJoinStep<Record> from = select.from(VIEW_TABLE_CONTAINERS);

			// Filter here!
			filter(from, filters);

			List<ViewTableContainers> result = setPaginationAndOrderBy(from).fetch().into(ViewTableContainers.class);

			long count = previousCount == -1 ? context.fetchOne("SELECT FOUND_ROWS()").into(Long.class) : previousCount;

			return new PaginatedResult<>(result, count);
		}
	}
}
