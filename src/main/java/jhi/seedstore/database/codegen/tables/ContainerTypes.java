/*
 * This file is generated by jOOQ.
 */
package jhi.seedstore.database.codegen.tables;


import java.sql.Timestamp;

import jhi.seedstore.database.codegen.SeedstoreDb;
import jhi.seedstore.database.codegen.tables.records.ContainerTypesRecord;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


// @formatter:off
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ContainerTypes extends TableImpl<ContainerTypesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>seedstore_db.container_types</code>
     */
    public static final ContainerTypes CONTAINER_TYPES = new ContainerTypes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ContainerTypesRecord> getRecordType() {
        return ContainerTypesRecord.class;
    }

    /**
     * The column <code>seedstore_db.container_types.id</code>.
     */
    public final TableField<ContainerTypesRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>seedstore_db.container_types.name</code>.
     */
    public final TableField<ContainerTypesRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>seedstore_db.container_types.description</code>.
     */
    public final TableField<ContainerTypesRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>seedstore_db.container_types.icon</code>.
     */
    public final TableField<ContainerTypesRecord, byte[]> ICON = createField(DSL.name("icon"), SQLDataType.BLOB, this, "");

    /**
     * The column <code>seedstore_db.container_types.created_on</code>.
     */
    public final TableField<ContainerTypesRecord, Timestamp> CREATED_ON = createField(DSL.name("created_on"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>seedstore_db.container_types.updated_on</code>.
     */
    public final TableField<ContainerTypesRecord, Timestamp> UPDATED_ON = createField(DSL.name("updated_on"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.TIMESTAMP)), this, "");

    private ContainerTypes(Name alias, Table<ContainerTypesRecord> aliased) {
        this(alias, aliased, null);
    }

    private ContainerTypes(Name alias, Table<ContainerTypesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>seedstore_db.container_types</code> table
     * reference
     */
    public ContainerTypes(String alias) {
        this(DSL.name(alias), CONTAINER_TYPES);
    }

    /**
     * Create an aliased <code>seedstore_db.container_types</code> table
     * reference
     */
    public ContainerTypes(Name alias) {
        this(alias, CONTAINER_TYPES);
    }

    /**
     * Create a <code>seedstore_db.container_types</code> table reference
     */
    public ContainerTypes() {
        this(DSL.name("container_types"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : SeedstoreDb.SEEDSTORE_DB;
    }

    @Override
    public Identity<ContainerTypesRecord, Integer> getIdentity() {
        return (Identity<ContainerTypesRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<ContainerTypesRecord> getPrimaryKey() {
        return Internal.createUniqueKey(ContainerTypes.CONTAINER_TYPES, DSL.name("KEY_container_types_PRIMARY"), new TableField[] { ContainerTypes.CONTAINER_TYPES.ID }, true);
    }

    @Override
    public ContainerTypes as(String alias) {
        return new ContainerTypes(DSL.name(alias), this);
    }

    @Override
    public ContainerTypes as(Name alias) {
        return new ContainerTypes(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ContainerTypes rename(String name) {
        return new ContainerTypes(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ContainerTypes rename(Name name) {
        return new ContainerTypes(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Integer, String, String, byte[], Timestamp, Timestamp> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
    // @formatter:on
}
