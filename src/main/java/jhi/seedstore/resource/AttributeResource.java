package jhi.seedstore.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jhi.seedstore.Database;
import jhi.seedstore.database.codegen.tables.pojos.Attributes;
import jhi.seedstore.util.Secured;
import org.jooq.DSLContext;

import java.sql.*;
import java.util.List;

import static jhi.seedstore.database.codegen.tables.Attributes.*;

@Path("attribute")
@Secured
@PermitAll
public class AttributeResource
{
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Attributes> getAttributes()
		throws SQLException
	{
		try (Connection conn = Database.getConnection())
		{
			DSLContext context = Database.getContext(conn);

			return context.selectFrom(ATTRIBUTES).orderBy(ATTRIBUTES.NAME).fetchInto(Attributes.class);
		}
	}
}
