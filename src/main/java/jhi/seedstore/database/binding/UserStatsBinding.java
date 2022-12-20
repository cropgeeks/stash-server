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
public class UserStatsBinding implements Binding<JSON, Map<String, Integer>>
{
	@Override
	public Converter<JSON, Map<String, Integer>> converter()
	{
		Gson gson = new Gson();
		return new Converter<>()
		{
			@Override
			public Map<String, Integer> from(JSON o)
			{
				return o == null ? null : gson.fromJson(Objects.toString(o), new TypeToken<Map<String, Integer>>(){}.getType());
			}

			@Override
			public JSON to(Map<String, Integer> o)
			{
				return o == null ? null : JSON.json(gson.toJson(o));
			}

			@Override
			public Class<JSON> fromType()
			{
				return JSON.class;
			}

			@Override
			public Class<Map<String, Integer>> toType()
			{
				return (Class) Map.class;
			}
		};
	}

	@Override
	public void sql(BindingSQLContext<Map<String, Integer>> ctx)
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
	public void register(BindingRegisterContext<Map<String, Integer>> ctx)
		throws SQLException
	{
		ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
	}

	@Override
	public void set(BindingSetStatementContext<Map<String, Integer>> ctx)
		throws SQLException
	{
		ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
	}

	@Override
	public void set(BindingSetSQLOutputContext<Map<String, Integer>> ctx)
		throws SQLException
	{
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void get(BindingGetResultSetContext<Map<String, Integer>> ctx)
		throws SQLException
	{
		ctx.convert(converter()).value(JSON.json(ctx.resultSet().getString(ctx.index())));
	}

	@Override
	public void get(BindingGetStatementContext<Map<String, Integer>> ctx)
		throws SQLException
	{
		ctx.convert(converter()).value(JSON.json(ctx.statement().getString(ctx.index())));
	}

	@Override
	public void get(BindingGetSQLInputContext<Map<String, Integer>> ctx)
		throws SQLException
	{
		throw new SQLFeatureNotSupportedException();
	}
}
