package jhi.seedstore.database.binding;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jooq.*;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;

import java.sql.*;
import java.util.*;

/**
 * @author Sebastian Raubach
 */
public class ContainerAttributeBinding implements Binding<JSON, Map<String, String>>
{
	@Override
	public Converter<JSON, Map<String, String>> converter()
	{
		Gson gson = new Gson();
		return new Converter<>()
		{
			@Override
			public Map<String, String> from(JSON o)
			{
				return o == null ? null : gson.fromJson(Objects.toString(o), new TypeToken<Map<String, String>>(){}.getType());
			}

			@Override
			public JSON to(Map<String, String> o)
			{
				return o == null ? null : JSON.json(gson.toJson(o));
			}

			@Override
			public Class<JSON> fromType()
			{
				return JSON.class;
			}

			@Override
			public Class<Map<String, String>> toType()
			{
				return (Class) Map.class;
			}
		};
	}

	@Override
	public void sql(BindingSQLContext<Map<String, String>> ctx)
		throws SQLException
	{
		// Depending on how you generate your SQL, you may need to explicitly distinguish
		// between jOOQ generating bind variables or inlined literals.
		if (ctx.render().paramType() == ParamType.INLINED)
			ctx.render().visit(DSL.inline(ctx.convert(converter()).value())).sql("");
		else
			ctx.render().sql("?");
	}

	@Override
	public void register(BindingRegisterContext<Map<String, String>> ctx)
		throws SQLException
	{
		ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
	}

	@Override
	public void set(BindingSetStatementContext<Map<String, String>> ctx)
		throws SQLException
	{
		ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
	}

	@Override
	public void set(BindingSetSQLOutputContext<Map<String, String>> ctx)
		throws SQLException
	{
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void get(BindingGetResultSetContext<Map<String, String>> ctx)
		throws SQLException
	{
		ctx.convert(converter()).value(JSON.json(ctx.resultSet().getString(ctx.index())));
	}

	@Override
	public void get(BindingGetStatementContext<Map<String, String>> ctx)
		throws SQLException
	{
		ctx.convert(converter()).value(JSON.json(ctx.statement().getString(ctx.index())));
	}

	@Override
	public void get(BindingGetSQLInputContext<Map<String, String>> ctx)
		throws SQLException
	{
		throw new SQLFeatureNotSupportedException();
	}
}
