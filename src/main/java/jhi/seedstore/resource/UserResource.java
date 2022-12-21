package jhi.seedstore.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.enums.UsersUserType;
import jhi.seedstore.database.codegen.tables.pojos.*;
import jhi.seedstore.database.codegen.tables.records.UsersRecord;
import jhi.seedstore.pojo.*;
import jhi.seedstore.resource.base.BaseResource;
import jhi.seedstore.util.*;
import jhi.seedstore.util.auth.BCrypt;
import org.jooq.*;

import java.sql.*;
import java.util.*;

import static jhi.seedstore.database.codegen.tables.Users.*;
import static jhi.seedstore.database.codegen.tables.ViewTableUsers.*;

@Path("user")
public class UserResource extends BaseResource
{
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.admin)
	public Response postUser(Users newUser)
		throws SQLException
	{
		if (newUser == null || newUser.getId() != null || StringUtils.isAnyEmpty(newUser.getName(), newUser.getEmailAddress(), newUser.getPasswordHash()) || newUser.getUserType() == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			// Email address already in use by a different user
			boolean emailAlreadyExists = context.fetchExists(context.selectOne().from(USERS).where(USERS.EMAIL_ADDRESS.eq(newUser.getEmailAddress())));
			if (emailAlreadyExists)
				return Response.status(Response.Status.CONFLICT).build();

			UsersRecord record = context.newRecord(USERS, newUser);

			String hashedPw = BCrypt.hashpw(newUser.getPasswordHash(), BCrypt.gensalt(TokenResource.SALT));
			record.setPasswordHash(hashedPw);
			return Response.ok(record.store() > 0).build();
		}
	}

	@PATCH
	@Path("/{userId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.admin)
	public Response patchUser(@PathParam("userId") Integer userId, ViewTableUsers update)
		throws SQLException
	{
		if (update == null || update.getId() == null || !Objects.equals(update.getId(), userId) || StringUtils.isAnyEmpty(update.getName(), update.getEmailAddress()) || update.getUserType() == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			// Check user exists
			UsersRecord existingUser = context.selectFrom(USERS).where(USERS.ID.eq(userId)).fetchAny();
			if (existingUser == null)
				return Response.status(Response.Status.NOT_FOUND).build();

			// Email address already in use by a different user
			boolean emailAlreadyExists = context.fetchExists(context.selectOne().from(USERS).where(USERS.EMAIL_ADDRESS.eq(update.getEmailAddress())).and(USERS.ID.notEqual(userId)));
			if (emailAlreadyExists)
				return Response.status(Response.Status.CONFLICT).build();

			// Invalid user type
			UsersUserType userType = UsersUserType.lookupLiteral(update.getUserType().getLiteral());
			if (userType == null)
				return Response.status(Response.Status.BAD_REQUEST).build();

			// Update
			existingUser.setName(update.getName());
			existingUser.setEmailAddress(update.getEmailAddress());
			existingUser.setUserType(userType);
			existingUser.store();

			return Response.ok(context.selectFrom(VIEW_TABLE_USERS).where(VIEW_TABLE_USERS.ID.eq(userId)).fetchAnyInto(ViewTableUsers.class)).build();
		}
	}

	@POST
	@Path("/table")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.admin)
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
	@Path("/{userId:\\d+}/img")
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
