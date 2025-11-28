package jhi.seedstore.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.enums.UsersUserType;
import jhi.seedstore.database.codegen.tables.pojos.ViewTableContainers;
import jhi.seedstore.database.codegen.tables.records.*;
import jhi.seedstore.pojo.ContainerImport;
import jhi.seedstore.resource.base.ContextResource;
import jhi.seedstore.util.*;
import org.jooq.DSLContext;

import java.sql.*;
import java.util.*;

import static jhi.seedstore.database.codegen.tables.Containers.CONTAINERS;
import static jhi.seedstore.database.codegen.tables.TransferLogs.TRANSFER_LOGS;

@Path("import")
public class ImportResource extends ContextResource
{
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.regular)
	public Response postContainerImport(ContainerImport cImport)
			throws SQLException
	{
		AuthenticationFilter.UserDetails sessionUser = (AuthenticationFilter.UserDetails) securityContext.getUserPrincipal();

		if (cImport == null || cImport.getParentContainerId() == null || CollectionUtils.isEmpty(cImport.getItems()))
			return Response.status(Response.Status.BAD_REQUEST).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			ContainersRecord parent = context.selectFrom(CONTAINERS).where(CONTAINERS.ID.eq(cImport.getParentContainerId())).fetchAny();

			if (parent == null)
				return Response.status(Response.Status.NOT_FOUND).build();

			Integer parentId = parent.getId();
			List<Integer> containersToMove = cImport.getItems().stream().map(ViewTableContainers::getContainerId).filter(Objects::nonNull).toList();

			moveContainers(sessionUser, parentId, containersToMove);

			return Response.ok().build();
		}
		catch (StashException e)
		{
			return Response.status(e.getStatus().getStatusCode(), e.getMessage()).build();
		}
	}

	public static void moveContainers(AuthenticationFilter.UserDetails sessionUser, Integer parentId, List<Integer> containersToMove)
			throws SQLException, StashException
	{
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			// For each existing item, move it to the new parent container
			for (Integer containerId : containersToMove)
			{
				// Get source and target and check they exist
				ContainersRecord existing = context.selectFrom(CONTAINERS).where(CONTAINERS.ID.eq(containerId)).fetchAny();
				ContainersRecord target = context.selectFrom(CONTAINERS).where(CONTAINERS.ID.eq(parentId)).fetchAny();
				if (existing == null || target == null)
					throw new StashException(Response.Status.NOT_FOUND);

				TransferLogsRecord tl = context.newRecord(TRANSFER_LOGS);
				tl.setContainerId(existing.getId());
				if (existing.getParentContainerId() != null)
					tl.setSourceId(existing.getParentContainerId());
				tl.setTargetId(target.getId());
				tl.setUserId(sessionUser.getId());
				tl.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				tl.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
				tl.store();

				existing.setParentContainerId(parentId);
				existing.store(CONTAINERS.PARENT_CONTAINER_ID);
			}
		}
	}
}
