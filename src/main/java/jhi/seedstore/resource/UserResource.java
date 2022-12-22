package jhi.seedstore.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.enums.UsersUserType;
import jhi.seedstore.database.codegen.tables.pojos.ViewTableUsers;
import jhi.seedstore.database.codegen.tables.records.UsersRecord;
import jhi.seedstore.pojo.*;
import jhi.seedstore.resource.base.BaseResource;
import jhi.seedstore.util.*;
import jhi.seedstore.util.auth.BCrypt;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.*;
import org.jooq.*;

import java.io.*;
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
	public Response postUser(BasicUser newUser)
		throws SQLException
	{
		if (newUser == null || newUser.getId() != null || StringUtils.isAnyEmpty(newUser.getName(), newUser.getEmailAddress(), newUser.getPassword()) || newUser.getUserType() == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);
			// Email address already in use by a different user
			boolean emailAlreadyExists = context.fetchExists(context.selectOne().from(USERS).where(USERS.EMAIL_ADDRESS.eq(newUser.getEmailAddress())));
			if (emailAlreadyExists)
				return Response.status(Response.Status.CONFLICT).build();

			UsersRecord record = context.newRecord(USERS, newUser);

			String hashedPw = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt(TokenResource.SALT));
			record.setPasswordHash(hashedPw);
			return Response.ok(record.store() > 0).build();
		}
	}

	@POST
	@Path("/{userId:\\d+}/img")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postUserImage(@PathParam("userId") Integer userId, @FormDataParam("image") InputStream fileIs, @FormDataParam("image") FormDataContentDisposition fileDetails)
		throws SQLException, IOException
	{
		if (userId == null || fileIs == null || fileDetails == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		if (fileDetails.getSize() >= 4194304)
			return Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			UsersRecord record = context.selectFrom(USERS).where(USERS.ID.eq(userId)).fetchAny();

			if (record == null)
				return Response.status(Response.Status.NOT_FOUND).build();

			record.setIcon(IOUtils.toByteArray(fileIs));
			return Response.ok(record.store(USERS.ICON) > 0).build();
		}
	}

	@PATCH
	@Path("/{userId:\\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured(UsersUserType.admin)
	public Response patchUser(@PathParam("userId") Integer userId, BasicUser update)
		throws SQLException
	{
		if (update == null || update.getId() == null || !Objects.equals(update.getId(), userId))
			return Response.status(Response.Status.BAD_REQUEST).build();

		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			// Check user exists
			UsersRecord existingUser = context.selectFrom(USERS).where(USERS.ID.eq(userId)).fetchAny();
			if (existingUser == null)
				return Response.status(Response.Status.NOT_FOUND).build();

			if (!StringUtils.isEmpty(update.getEmailAddress()))
			{
				// Email address already in use by a different user
				boolean emailAlreadyExists = context.fetchExists(context.selectOne().from(USERS).where(USERS.EMAIL_ADDRESS.eq(update.getEmailAddress())).and(USERS.ID.notEqual(userId)));
				if (emailAlreadyExists)
					return Response.status(Response.Status.CONFLICT).build();
			}

			UsersUserType userType = null;
			if (update.getUserType() != null)
			{
				// Invalid user type
				userType = UsersUserType.lookupLiteral(update.getUserType().getLiteral());
				if (userType == null)
					return Response.status(Response.Status.BAD_REQUEST).build();
			}

			// Update
			if (!StringUtils.isEmpty(update.getName()))
				existingUser.setName(update.getName());
			if (!StringUtils.isEmpty(update.getEmailAddress()))
				existingUser.setEmailAddress(update.getEmailAddress());
			if (userType != null)
				existingUser.setUserType(userType);
			if (existingUser.changed())
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
	@Produces({"image/png", "image/jpeg", "image/*"})
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
