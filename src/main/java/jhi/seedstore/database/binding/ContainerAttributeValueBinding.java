package jhi.seedstore.database.binding;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jhi.seedstore.pojo.ContainerAttributeValue;
import org.jooq.*;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;

import java.sql.*;
import java.util.*;

/**
 * @author Sebastian Raubach
 */
public class ContainerAttributeValueBinding implements Binding<JSON, ContainerAttributeValue[]>
{
	@Override
	public Converter<JSON, ContainerAttributeValue[]> converter()
	{
		Gson gson = new Gson();
		return new Converter<>()
		{
			@Override
			public ContainerAttributeValue[] from(JSON o)
			{
				return o == null ? null : gson.fromJson(Objects.toString(o), ContainerAttributeValue[].class);
			}

			@Override
			public JSON to(ContainerAttributeValue[] o)
			{
				return o == null ? null : JSON.json(gson.toJson(o));
			}

			@Override
			public Class<JSON> fromType()
			{
				return JSON.class;
			}

			@Override
			public Class<ContainerAttributeValue[]> toType()
			{
				return ContainerAttributeValue[].class;
			}
		};
	}

	@Override
	public void sql(BindingSQLContext<ContainerAttributeValue[]> ctx)
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
	public void register(BindingRegisterContext<ContainerAttributeValue[]> ctx)
		throws SQLException
	{
		ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
	}

	@Override
	public void set(BindingSetStatementContext<ContainerAttributeValue[]> ctx)
		throws SQLException
	{
		ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
	}

	@Override
	public void set(BindingSetSQLOutputContext<ContainerAttributeValue[]> ctx)
		throws SQLException
	{
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void get(BindingGetResultSetContext<ContainerAttributeValue[]> ctx)
		throws SQLException
	{
		ctx.convert(converter()).value(JSON.json(ctx.resultSet().getString(ctx.index())));
	}

	@Override
	public void get(BindingGetStatementContext<ContainerAttributeValue[]> ctx)
		throws SQLException
	{
		ctx.convert(converter()).value(JSON.json(ctx.statement().getString(ctx.index())));
	}

	@Override
	public void get(BindingGetSQLInputContext<ContainerAttributeValue[]> ctx)
		throws SQLException
	{
		throw new SQLFeatureNotSupportedException();
	}
}
