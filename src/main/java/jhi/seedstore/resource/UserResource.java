package jhi.seedstore.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.tables.records.UsersRecord;
import jhi.seedstore.resource.base.ContextResource;
import jhi.seedstore.util.*;
import org.jooq.DSLContext;

import java.sql.*;

import static jhi.seedstore.database.codegen.tables.Users.*;

@Path("user")
public class UserResource extends ContextResource
{
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
