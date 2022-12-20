package jhi.seedstore.database.binding;

import com.google.gson.Gson;
import org.jooq.*;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;

import java.sql.*;
import java.util.Objects;

public class IntArrayBinding implements Binding<JSON, Integer[]>
{
	@Override
	public Converter<JSON, Integer[]> converter()
	{
		Gson gson = new Gson();
		return new Converter<>()
		{
			@Override
			public Integer[] from(JSON o)
			{
				return o == null ? null : gson.fromJson(Objects.toString(o), Integer[].class);
			}

			@Override
			public JSON to(Integer[] o)
			{
				return o == null ? null : JSON.json(gson.toJson(o));
			}

			@Override
			public Class<JSON> fromType()
			{
				return JSON.class;
			}

			@Override
			public Class<Integer[]> toType()
			{
				return Integer[].class;
			}
		};
	}

	@Override
	public void sql(BindingSQLContext<Integer[]> ctx)
		throws SQLException
	{
		// Depending on how you generate your SQL, you may need to explicitly distinguish
		// between jOOQ generating bind variables or inlined literals.
		if (ctx.render().paramType() == ParamType.INLINED)
			ctx.render().visit(DSL.inline(ctx.convert(converter()).value())).sql("");
		else
			ctx.render().visit(DSL.sql("?"));
	}

	@Override
	public void register(BindingRegisterContext<Integer[]> ctx)
		throws SQLException
	{
		ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
	}

	@Override
	public void set(BindingSetStatementContext<Integer[]> ctx)
		throws SQLException
	{
		ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
	}

	@Override
	public void set(BindingSetSQLOutputContext<Integer[]> ctx)
		throws SQLException
	{
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void get(BindingGetResultSetContext<Integer[]> ctx)
		throws SQLException
	{
		ctx.convert(converter()).value(JSON.json(ctx.resultSet().getString(ctx.index())));
	}

	@Override
	public void get(BindingGetStatementContext<Integer[]> ctx)
		throws SQLException
	{
		ctx.convert(converter()).value(JSON.json(ctx.statement().getString(ctx.index())));
	}

	@Override
	public void get(BindingGetSQLInputContext<Integer[]> ctx)
		throws SQLException
	{
		throw new SQLFeatureNotSupportedException();
	}
}
