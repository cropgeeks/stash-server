package jhi.seedstore.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.*;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.enums.UsersUserType;
import jhi.seedstore.database.codegen.tables.pojos.*;
import jhi.seedstore.database.codegen.tables.records.*;
import jhi.seedstore.pojo.*;
import jhi.seedstore.resource.base.BaseResource;
import jhi.seedstore.util.*;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.stream.Collectors;

import static jhi.seedstore.database.codegen.tables.Attributes.ATTRIBUTES;
import static jhi.seedstore.database.codegen.tables.ContainerAttributes.CONTAINER_ATTRIBUTES;
import static jhi.seedstore.database.codegen.tables.ContainerTypes.CONTAINER_TYPES;
import static jhi.seedstore.database.codegen.tables.Containers.CONTAINERS;
import static jhi.seedstore.database.codegen.tables.Projects.PROJECTS;
import static jhi.seedstore.database.codegen.tables.TransferLogs.TRANSFER_LOGS;
import static jhi.seedstore.database.codegen.tables.Trials.TRIALS;
import static jhi.seedstore.database.codegen.tables.ViewTableContainers.VIEW_TABLE_CONTAINERS;

@Path("container")
public class ContainerResource extends BaseResource
{
	@GET
	@Path("/{containerId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured
	@PermitAll
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
	@Secured(UsersUserType.admin)
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

	@GET
	@Path("/{containerId:\\d+}/attribute")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured
	@PermitAll
	public Response getContainerAttributes(@PathParam("containerId") Integer containerId)
			throws SQLException
	{
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			return Response.ok(context.selectFrom(CONTAINER_ATTRIBUTES)
									  .where(CONTAINER_ATTRIBUTES.CONTAINER_ID.eq(containerId))
									  .orderBy(CONTAINER_ATTRIBUTES.CREATED_ON.desc())
									  .fetchInto(ContainerAttributes.class)).build();
		}
	}

	@DELETE
	@Path("/{containerId:\\d+}/attribute/{containerAttributeValuesId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.admin)
	public Response postContainerAttributes(@PathParam("containerId") Integer containerId, @PathParam("containerAttributeValuesId") Integer containerAttributeValuesId)
			throws SQLException
	{
		if (containerId == null || containerAttributeValuesId == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			ContainerAttributesRecord record = context.selectFrom(CONTAINER_ATTRIBUTES).where(CONTAINER_ATTRIBUTES.CONTAINER_ID.eq(containerId)).and(CONTAINER_ATTRIBUTES.ID.eq(containerAttributeValuesId)).fetchAny();

			if (record == null)
				return Response.status(Response.Status.BAD_REQUEST).build();

			record.delete();

			return Response.ok().build();
		}
	}

	@POST
	@Path("/{containerId:\\d+}/attribute")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.admin)
	public Response postContainerAttributes(@PathParam("containerId") Integer containerId, ContainerAttributes containerAttributes)
			throws SQLException
	{
		if (containerId == null || containerAttributes == null || containerAttributes.getContainerId() == null || containerAttributes.getAttributeValues() == null || containerAttributes.getAttributeValues().isEmpty() || !containerId.equals(containerAttributes.getContainerId()))
			return Response.status(Response.Status.BAD_REQUEST).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			// Check the requested container exists
			Containers container = context.selectFrom(CONTAINERS).where(CONTAINERS.ID.eq(containerId)).fetchAnyInto(Containers.class);
			if (container == null)
				return Response.status(Response.Status.NOT_FOUND).build();

			Map<Integer, String> containerAttributeValues = containerAttributes.getAttributeValues();

			// Remove anything that isn't set (null, "", "   ", etc)
			Iterator<Map.Entry<Integer, String>> iterator = containerAttributeValues.entrySet().iterator();
			while (iterator.hasNext())
			{
				String value = iterator.next().getValue();

				if (StringUtils.isEmpty(value))
					iterator.remove();
			}

			// If no valid value is left, throw an error
			if (containerAttributes.getAttributeValues().isEmpty())
				return Response.status(Response.Status.BAD_REQUEST).build();

			// Check all the attributes exist
			Integer count = context.selectCount().from(ATTRIBUTES).where(ATTRIBUTES.ID.in(containerAttributes.getAttributeValues().keySet())).fetchAnyInto(Integer.class);
			if (count == null || count != containerAttributes.getAttributeValues().size())
				return Response.status(Response.Status.BAD_REQUEST).build();

			// Finally save the result
			ContainerAttributesRecord record = context.newRecord(CONTAINER_ATTRIBUTES, containerAttributes);
			if (record.getCreatedOn() == null)
				record.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			record.store();

			return Response.ok().build();
		}
	}

	@DELETE
	@Path("/{containerId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.admin)
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
	@Path("/{containerId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.regular)
	public Response postContainersIntoParent(@PathParam("containerId") Integer containerId, List<ViewTableContainers> containers)
			throws SQLException
	{
		AuthenticationFilter.UserDetails sessionUser = (AuthenticationFilter.UserDetails) securityContext.getUserPrincipal();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			Containers parent = context.selectFrom(CONTAINERS).where(CONTAINERS.ID.eq(containerId)).fetchAnyInto(Containers.class);

			if (parent == null)
				return Response.status(Response.Status.NOT_FOUND).build();

			List<Integer> containerIds = addNewContainers(containers);

			Integer parentId = parent.getId();

			ImportResource.moveContainers(sessionUser, parentId, containerIds);

			return Response.ok().build();
		}
		catch (StashException e)
		{
			return Response.status(e.getStatus().getStatusCode(), e.getMessage()).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.regular)
	public Response postContainer(List<ViewTableContainers> containers)
			throws SQLException
	{
		if (CollectionUtils.isEmpty(containers))
			return Response.status(Response.Status.BAD_REQUEST).build();

		try
		{
			return Response.ok(addNewContainers(containers)).build();
		}
		catch (StashException e)
		{
			return Response.status(e.getStatus().getStatusCode(), e.getMessage()).build();
		}
	}

	private List<Integer> addNewContainers(List<ViewTableContainers> containers)
			throws SQLException, StashException
	{
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			for (ViewTableContainers container : containers)
			{
				// Payload empty or required field empty
				if (container == null || StringUtils.isEmpty(container.getContainerBarcode()) || container.getContainerTypeId() == null || container.getContainerIsActive() == null)
				{
					throw new StashException(Response.Status.BAD_REQUEST, "Either the container is null or the barcode or description aren't set or the container type or isActive state aren't set.");
				}

				// Barcode or description too long
				if (container.getContainerBarcode().length() > 100)
				{
					throw new StashException(Response.Status.REQUEST_ENTITY_TOO_LARGE);
				}

				List<String> barcodes = containers.stream().map(ViewTableContainers::getContainerBarcode).collect(Collectors.toList());

				int count = context.fetchCount(context.selectFrom(CONTAINERS).where(CONTAINERS.BARCODE.in(barcodes)));

				if (count > 0)
				{
					throw new StashException(Response.Status.CONFLICT);
				}
			}

			Map<Integer, Attributes> attributes = new HashMap<>();
			Set<Integer> typeIds = new HashSet<>();
			Set<Integer> projectIds = new HashSet<>();
			Set<Integer> trialIds = new HashSet<>();
			Set<Integer> parentIds = new HashSet<>();
			for (ViewTableContainers c : containers)
			{
				if (c.getContainerTypeId() != null) typeIds.add(c.getContainerTypeId());
				if (c.getProjectId() != null) projectIds.add(c.getProjectId());
				if (c.getTrialId() != null) trialIds.add(c.getTrialId());
				if (c.getParentId() != null) parentIds.add(c.getParentId());
				if (!CollectionUtils.isEmpty(c.getContainerAttributes()))
				{
					for (ContainerAttributeTimeline a : c.getContainerAttributes())
					{
						String date = a.getDate();

						try
						{
							sdf.parse(date);
						}
						catch (ParseException e)
						{
							throw new StashException(Response.Status.EXPECTATION_FAILED, date);
						}

						for (Map.Entry<Integer, String> entry : a.getAttributeValues().entrySet())
						{
							if (entry.getKey() == null)
							{
								throw new StashException(Response.Status.BAD_REQUEST, "The attribute name or value of a specified attribute mapping is empty.");
							}
							else
							{
								Attributes attr = attributes.get(entry.getKey());
								if (attr == null)
								{
									attr = context.selectFrom(ATTRIBUTES).where(ATTRIBUTES.ID.eq(entry.getKey())).fetchAnyInto(Attributes.class);

									if (attr == null)
										throw new StashException(Response.Status.NOT_FOUND);
									else
										attributes.put(attr.getId(), attr);
								}

								switch (attr.getDatatype())
								{
									case numeric:
										try
										{
											Double.parseDouble(entry.getValue());
										}
										catch (NumberFormatException e)
										{
											throw new StashException(Response.Status.EXPECTATION_FAILED, entry.getValue());
										}
										break;
									case date:
										try
										{
											sdf.parse(entry.getValue());
										}
										catch (ParseException e)
										{
											throw new StashException(Response.Status.EXPECTATION_FAILED, entry.getValue());
										}
										break;
								}
							}
						}
					}
				}
			}

			// Check referenced entities
			boolean typeExists = context.fetchCount(context.selectFrom(CONTAINER_TYPES).where(CONTAINER_TYPES.ID.in(typeIds))) == typeIds.size();
			boolean projectExists = context.fetchCount(context.selectFrom(PROJECTS).where(PROJECTS.ID.in(projectIds))) == projectIds.size();
			boolean trialExists = context.fetchCount(context.selectFrom(TRIALS).where(TRIALS.ID.in(trialIds))) == trialIds.size();
			boolean parentExists = context.fetchCount(context.selectFrom(CONTAINERS).where(CONTAINERS.ID.in(parentIds))) == parentIds.size();

			if (!typeExists)
				throw new StashException(Response.Status.NOT_FOUND, "Container type not found.");
			if (!projectExists)
				throw new StashException(Response.Status.NOT_FOUND, "Project not found.");
			if (!trialExists)
				throw new StashException(Response.Status.NOT_FOUND, "Trial not found.");
			if (!parentExists)
				throw new StashException(Response.Status.NOT_FOUND, "Parent container not found.");

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
//				c.setDescription(container.getContainerDescription());
				c.setBarcode(container.getContainerBarcode());
				c.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				c.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
				c.store();
				ids.add(c.getId());

				if (!CollectionUtils.isEmpty(container.getContainerAttributes()))
				{
					for (ContainerAttributeTimeline attributeValueDate : container.getContainerAttributes())
					{
						String date = attributeValueDate.getDate();

						ContainerAttributesRecord a = context.newRecord(CONTAINER_ATTRIBUTES);
						a.setContainerId(c.getId());
						try
						{
							a.setCreatedOn(new Timestamp(sdf.parse(date).getTime()));
						}
						catch (ParseException e)
						{
							a.setCreatedOn(new Timestamp(System.currentTimeMillis()));
						}
						a.setAttributeValues(attributeValueDate.getAttributeValues());
						a.store();
					}
				}
			}

			// Return the id
			return ids;
		}
	}

	@POST
	@Path("/table")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured
	@PermitAll
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
			where(from, filters, true);

			List<ViewTableContainers> result = setPaginationAndOrderBy(from).fetch().into(ViewTableContainers.class);

			long count = previousCount == -1 ? context.fetchOne("SELECT FOUND_ROWS()").into(Long.class) : previousCount;

			return new PaginatedResult<>(result, count);
		}
	}
}
