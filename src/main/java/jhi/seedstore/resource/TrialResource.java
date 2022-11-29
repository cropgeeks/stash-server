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

import static jhi.seedstore.database.codegen.tables.Projects.*;
import static jhi.seedstore.database.codegen.tables.Trials.*;

@Path("trial")
@Secured
@PermitAll
public class TrialResource extends BaseResource
{
	@GET
	@Path("/{trialId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Trials getTrial(@PathParam("trialId") Integer trialId)
		throws SQLException
	{
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectWhereStep<TrialsRecord> from = context.selectFrom(TRIALS);

			if (trialId != null)
				from.where(TRIALS.ID.eq(trialId));

			return from.fetchAnyInto(Trials.class);
		}
	}

	@DELETE
	@Path("/{trialId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTrial(@PathParam("trialId") Integer trialId)
		throws SQLException
	{
		if (trialId == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			return Response.ok(context.deleteFrom(TRIALS).where(TRIALS.ID.eq(trialId)).execute() > 0).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postTrial(Trials trial)
		throws SQLException
	{
		if (trial == null || StringUtils.isEmpty(trial.getName()) || trial.getProjectId() == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		if (trial.getId() != null)
			return Response.status(Response.Status.CONFLICT).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			ProjectsRecord project = context.selectFrom(PROJECTS).where(PROJECTS.ID.eq(trial.getProjectId())).fetchAny();

			if (project == null)
				return Response.status(Response.Status.NOT_FOUND).build();

			TrialsRecord record = context.selectFrom(TRIALS).where(TRIALS.NAME.eq(trial.getName())).fetchAny();

			if (record != null)
				return Response.status(Response.Status.CONFLICT).build();

			record = context.newRecord(TRIALS, trial);
			record.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			record.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
			record.store();

			// Return the id
			return Response.ok(record.into(Trials.class)).build();
		}
	}

	@POST
	@Path("/table")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PaginatedResult<List<Trials>> postContainerTable(PaginatedRequest request)
		throws SQLException
	{
		processRequest(request);
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectSelectStep<Record> select = context.select();

			if (previousCount == -1)
				select.hint("SQL_CALC_FOUND_ROWS");

			SelectJoinStep<Record> from = select.from(TRIALS);

			// Filter here!
			filter(from, filters);

			List<Trials> result = setPaginationAndOrderBy(from)
				.fetch()
				.into(Trials.class);

			long count = previousCount == -1 ? context.fetchOne("SELECT FOUND_ROWS()").into(Long.class) : previousCount;

			return new PaginatedResult<>(result, count);
		}
	}
}
