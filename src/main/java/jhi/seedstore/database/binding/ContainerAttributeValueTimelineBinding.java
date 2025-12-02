package jhi.seedstore.database.binding;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jhi.seedstore.pojo.ContainerAttributeTimeline;
import org.jooq.*;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;

import java.sql.*;
import java.util.*;

/**
 * @author Sebastian Raubach
 */
public class ContainerAttributeValueTimelineBinding implements Binding<JSON, ContainerAttributeTimeline[]>
{
	@Override
	public Converter<JSON, ContainerAttributeTimeline[]> converter()
	{
		Gson gson = new Gson();
		return new Converter<>()
		{
			@Override
			public ContainerAttributeTimeline[] from(JSON o)
			{
				return o == null ? null : gson.fromJson(Objects.toString(o), ContainerAttributeTimeline[].class);
			}

			@Override
			public JSON to(ContainerAttributeTimeline[] o)
			{
				return o == null ? null : JSON.json(gson.toJson(o));
			}

			@Override
			public Class<JSON> fromType()
			{
				return JSON.class;
			}

			@Override
			public Class<ContainerAttributeTimeline[]> toType()
			{
				return ContainerAttributeTimeline[].class;
			}
		};
	}

	@Override
	public void sql(BindingSQLContext<ContainerAttributeTimeline[]> ctx)
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
	public void register(BindingRegisterContext<ContainerAttributeTimeline[]> ctx)
			throws SQLException
	{
		ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
	}

	@Override
	public void set(BindingSetStatementContext<ContainerAttributeTimeline[]> ctx)
			throws SQLException
	{
		ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
	}

	@Override
	public void set(BindingSetSQLOutputContext<ContainerAttributeTimeline[]> ctx)
			throws SQLException
	{
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void get(BindingGetResultSetContext<ContainerAttributeTimeline[]> ctx)
			throws SQLException
	{
		ctx.convert(converter()).value(JSON.json(ctx.resultSet().getString(ctx.index())));
	}

	@Override
	public void get(BindingGetStatementContext<ContainerAttributeTimeline[]> ctx)
			throws SQLException
	{
		ctx.convert(converter()).value(JSON.json(ctx.statement().getString(ctx.index())));
	}

	@Override
	public void get(BindingGetSQLInputContext<ContainerAttributeTimeline[]> ctx)
			throws SQLException
	{
		throw new SQLFeatureNotSupportedException();
	}
}
