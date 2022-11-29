package jhi.seedstore.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.tables.pojos.ViewTableContainers;
import jhi.seedstore.database.codegen.tables.records.*;
import jhi.seedstore.pojo.ContainerImport;
import jhi.seedstore.resource.base.ContextResource;
import jhi.seedstore.util.*;
import org.jooq.DSLContext;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

import static jhi.seedstore.database.codegen.tables.Containers.*;
import static jhi.seedstore.database.codegen.tables.TransferLogs.*;

@Path("import")
@Secured
@PermitAll
public class ImportResource extends ContextResource
{
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
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

			List<ViewTableContainers> existingItems = cImport.getItems().stream().filter(c -> c.getContainerId() != null).collect(Collectors.toList());
			List<ViewTableContainers> newItems = cImport.getItems().stream().filter(c -> c.getContainerId() == null).collect(Collectors.toList());

			// Create the new items based on the provided information
			for (ViewTableContainers n : newItems)
			{
				ContainersRecord record = context.newRecord(CONTAINERS);
				record.setBarcode(n.getContainerBarcode());
				record.setContainerTypeId(n.getContainerTypeId());
				record.setParentContainerId(cImport.getParentContainerId());
				record.setDescription(n.getContainerDescription());
				record.setIsActive(true);
				record.setProjectId(n.getProjectId());
				record.setTrialId(n.getTrialId());
				record.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				record.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
				record.store();
			}

			// For each existing item, move it to the new parent container
			for (ViewTableContainers e : existingItems)
			{
				// Get source and target and check they exist
				ContainersRecord existing = context.selectFrom(CONTAINERS).where(CONTAINERS.ID.eq(e.getContainerId())).fetchAny();
				ContainersRecord target = context.selectFrom(CONTAINERS).where(CONTAINERS.ID.eq(cImport.getParentContainerId())).fetchAny();
				if (existing == null || target == null)
					return Response.status(Response.Status.NOT_FOUND).build();

				if (existing.getParentContainerId() != null)
				{
					TransferLogsRecord tl = context.newRecord(TRANSFER_LOGS);
					tl.setContainerId(existing.getId());
					tl.setSourceId(existing.getParentContainerId());
					tl.setTargetId(target.getId());
					tl.setUserId(sessionUser.getId());
					tl.setCreatedOn(new Timestamp(System.currentTimeMillis()));
					tl.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
					tl.store();
				}

				existing.setParentContainerId(cImport.getParentContainerId());
				existing.store(CONTAINERS.PARENT_CONTAINER_ID);
			}

			return Response.ok().build();
		}
	}
}
