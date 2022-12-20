package jhi.seedstore.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.tables.pojos.ViewTableUsers;
import jhi.seedstore.database.codegen.tables.records.UsersRecord;
import jhi.seedstore.pojo.*;
import jhi.seedstore.resource.base.BaseResource;
import jhi.seedstore.util.*;
import org.jooq.*;

import java.sql.*;
import java.util.List;

import static jhi.seedstore.database.codegen.tables.Users.*;
import static jhi.seedstore.database.codegen.tables.ViewTableUsers.*;

@Path("user")
public class UserResource extends BaseResource
{
	@POST
	@Path("/table")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured
	@PermitAll
	public PaginatedResult<List<ViewTableUsers>> postUserTable(PaginatedRequest request)
		throws SQLException
	{
		processRequest(request);
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			SelectSelectStep<Record> select = context.select();

			if (previousCount == -1)
				select.hint("SQL_CALC_FOUND_ROWS");

			SelectJoinStep<Record> from = select.from(VIEW_TABLE_USERS);

			// Filter here!
			filter(from, filters);

			List<ViewTableUsers> result = setPaginationAndOrderBy(from)
				.fetch()
				.into(ViewTableUsers.class);

			long count = previousCount == -1 ? context.fetchOne("SELECT FOUND_ROWS()").into(Long.class) : previousCount;

			return new PaginatedResult<>(result, count);
		}
	}

	@GET
	@Path("/{userId}/img")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserImage(@PathParam("userId") Integer userId, @QueryParam("imageToken") String imageToken)
		throws SQLException
	{
		// Check the image token
		if (!AuthenticationFilter.isValidImageToken(imageToken))
			return Response.status(Response.Status.FORBIDDEN).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			UsersRecord user = context.selectFrom(USERS).where(USERS.ID.eq(userId)).fetchAny();

			if (user == null || user.getIcon() == null)
				return Response.status(Response.Status.NOT_FOUND).build();
			else
				return Response.ok((StreamingOutput) output -> {
								   output.write(user.getIcon());
								   output.flush();
							   })
							   .type("image/png")
							   .build();
		}
	}
}
