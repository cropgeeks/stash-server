package jhi.seedstore.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.tables.pojos.*;
import jhi.seedstore.database.codegen.tables.records.ProjectsRecord;
import jhi.seedstore.pojo.*;
import jhi.seedstore.resource.base.BaseResource;
import jhi.seedstore.util.*;
import org.jooq.*;

import java.sql.*;
import java.util.List;

import static jhi.seedstore.database.codegen.tables.Projects.*;
import static jhi.seedstore.database.codegen.tables.ViewTableContainers.*;

@Path("project")
@Secured
@PermitAll
public class ProjectResource extends BaseResource
{
	@GET
	@Path("/{projectId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Projects getProject(@PathParam("projectId") Integer projectId)
		throws SQLException
	{
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectWhereStep<ProjectsRecord> from = context.selectFrom(PROJECTS);

			if (projectId != null)
				from.where(PROJECTS.ID.eq(projectId));

			return from.fetchAnyInto(Projects.class);
		}
	}

	@DELETE
	@Path("/{projectId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProject(@PathParam("projectId") Integer projectId)
		throws SQLException
	{
		if (projectId == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			return Response.ok(context.deleteFrom(PROJECTS).where(PROJECTS.ID.eq(projectId)).execute() > 0).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postProject(Projects project)
		throws SQLException
	{
		if (project == null || StringUtils.isEmpty(project.getName()))
			return Response.status(Response.Status.BAD_REQUEST).build();

		if (project.getId() != null)
			return Response.status(Response.Status.CONFLICT).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			ProjectsRecord record = context.selectFrom(PROJECTS).where(PROJECTS.NAME.eq(project.getName())).fetchAny();

			if (record != null)
				return Response.status(Response.Status.CONFLICT).build();

			record = context.newRecord(PROJECTS, project);
			record.store();

			// Return the id
			return Response.ok(record.getId()).build();
		}
	}

	@POST
	@Path("/table")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PaginatedResult<List<Projects>> postProjectTable(PaginatedRequest request)
		throws SQLException
	{
		processRequest(request);
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectSelectStep<Record> select = context.select();

			if (previousCount == -1)
				select.hint("SQL_CALC_FOUND_ROWS");

			SelectJoinStep<Record> from = select.from(PROJECTS);

			// Filter here!
			filter(from, filters);

			List<Projects> result = setPaginationAndOrderBy(from)
				.fetch()
				.into(Projects.class);

			long count = previousCount == -1 ? context.fetchOne("SELECT FOUND_ROWS()").into(Long.class) : previousCount;

			return new PaginatedResult<>(result, count);
		}
	}
}
