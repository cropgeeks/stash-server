package jhi.seedstore.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.tables.pojos.*;
import jhi.seedstore.database.codegen.tables.records.ContainersRecord;
import jhi.seedstore.pojo.*;
import jhi.seedstore.resource.base.BaseResource;
import jhi.seedstore.util.*;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.*;
import java.util.*;

import static jhi.seedstore.database.codegen.tables.Containers.*;
import static jhi.seedstore.database.codegen.tables.TransferLogs.*;
import static jhi.seedstore.database.codegen.tables.ViewTableTransferEvents.*;
import static jhi.seedstore.database.codegen.tables.ViewTableTransfers.*;

@Path("transfer")
@Secured
@PermitAll
public class TransferResource extends BaseResource
{
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postContainerTransfer(ContainerTransfer transfer)
		throws SQLException
	{
		AuthenticationFilter.UserDetails sessionUser = (AuthenticationFilter.UserDetails) securityContext.getUserPrincipal();

		if (transfer == null || transfer.getSourceId() == null || transfer.getTargetId() == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			// Get source and target and check they exist
			ContainersRecord source = context.selectFrom(CONTAINERS).where(CONTAINERS.ID.eq(transfer.getSourceId())).fetchAny();
			ContainersRecord target = context.selectFrom(CONTAINERS).where(CONTAINERS.ID.eq(transfer.getTargetId())).fetchAny();
			if (source == null || target == null)
				return Response.status(Response.Status.NOT_FOUND).build();

			// Check they are the same container type
			if (!Objects.equals(source.getContainerTypeId(), target.getContainerTypeId()))
				return Response.status(Response.Status.EXPECTATION_FAILED).build();

			// Create new entries in the transfer log
			context.insertInto(TRANSFER_LOGS, TRANSFER_LOGS.CONTAINER_ID, TRANSFER_LOGS.SOURCE_ID, TRANSFER_LOGS.TARGET_ID, TRANSFER_LOGS.USER_ID)
				   .select(DSL.select(CONTAINERS.ID, CONTAINERS.PARENT_CONTAINER_ID, DSL.inline(transfer.getTargetId(), Integer.class), DSL.inline(sessionUser.getId(), Integer.class))
							  .from(CONTAINERS)
							  .where(CONTAINERS.PARENT_CONTAINER_ID.eq(transfer.getSourceId()))
				   )
				   .execute();

			// Update their parent container id
			long count = context.update(CONTAINERS).set(CONTAINERS.PARENT_CONTAINER_ID, transfer.getTargetId()).where(CONTAINERS.PARENT_CONTAINER_ID.eq(transfer.getSourceId())).execute();

			return Response.ok(count).build();
		}
	}

	@POST
	@Path("/table")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PaginatedResult<List<ViewTableTransfers>> postContainerTable(PaginatedRequest request)
		throws SQLException
	{
		processRequest(request);
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectSelectStep<Record> select = context.select();

			if (previousCount == -1)
				select.hint("SQL_CALC_FOUND_ROWS");

			SelectJoinStep<Record> from = select.from(VIEW_TABLE_TRANSFERS);

			// Filter here!
			filter(from, filters);

			List<ViewTableTransfers> result = setPaginationAndOrderBy(from)
				.fetch()
				.into(ViewTableTransfers.class);

			long count = previousCount == -1 ? context.fetchOne("SELECT FOUND_ROWS()").into(Long.class) : previousCount;

			return new PaginatedResult<>(result, count);
		}
	}

	@POST
	@Path("/event/table")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PaginatedResult<List<ViewTableTransferEvents>> postContainerEventTable(PaginatedRequest request)
		throws SQLException
	{
		processRequest(request);
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectSelectStep<Record> select = context.select();

			if (previousCount == -1)
				select.hint("SQL_CALC_FOUND_ROWS");

			SelectJoinStep<Record> from = select.from(VIEW_TABLE_TRANSFER_EVENTS);

			// Filter here!
			filter(from, filters);

			List<ViewTableTransferEvents> result = setPaginationAndOrderBy(from)
				.fetch()
				.into(ViewTableTransferEvents.class);

			long count = previousCount == -1 ? context.fetchOne("SELECT FOUND_ROWS()").into(Long.class) : previousCount;

			return new PaginatedResult<>(result, count);
		}
	}
}
