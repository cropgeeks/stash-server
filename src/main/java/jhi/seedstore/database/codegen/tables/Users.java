/*
 * This file is generated by jOOQ.
 */
package jhi.seedstore.database.codegen.tables;


import java.sql.Timestamp;

import jhi.seedstore.database.codegen.SeedstoreDb;
import jhi.seedstore.database.codegen.enums.UsersUserType;
import jhi.seedstore.database.codegen.tables.records.UsersRecord;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Row9;
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
public class Users extends TableImpl<UsersRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>seedstore_db.users</code>
     */
    public static final Users USERS = new Users();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UsersRecord> getRecordType() {
        return UsersRecord.class;
    }

    /**
     * The column <code>seedstore_db.users.id</code>.
     */
    public final TableField<UsersRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>seedstore_db.users.name</code>.
     */
    public final TableField<UsersRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>seedstore_db.users.email_address</code>.
     */
    public final TableField<UsersRecord, String> EMAIL_ADDRESS = createField(DSL.name("email_address"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>seedstore_db.users.password_hash</code>.
     */
    public final TableField<UsersRecord, String> PASSWORD_HASH = createField(DSL.name("password_hash"), SQLDataType.VARCHAR(60), this, "");

    /**
     * The column <code>seedstore_db.users.icon</code>.
     */
    public final TableField<UsersRecord, byte[]> ICON = createField(DSL.name("icon"), SQLDataType.BLOB, this, "");

    /**
     * The column <code>seedstore_db.users.user_type</code>.
     */
    public final TableField<UsersRecord, UsersUserType> USER_TYPE = createField(DSL.name("user_type"), SQLDataType.VARCHAR(9).nullable(false).defaultValue(DSL.inline("reference", SQLDataType.VARCHAR)).asEnumDataType(jhi.seedstore.database.codegen.enums.UsersUserType.class), this, "");

    /**
     * The column <code>seedstore_db.users.last_login</code>.
     */
    public final TableField<UsersRecord, Timestamp> LAST_LOGIN = createField(DSL.name("last_login"), SQLDataType.TIMESTAMP(0), this, "");

    /**
     * The column <code>seedstore_db.users.created_on</code>.
     */
    public final TableField<UsersRecord, Timestamp> CREATED_ON = createField(DSL.name("created_on"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>seedstore_db.users.updated_on</code>.
     */
    public final TableField<UsersRecord, Timestamp> UPDATED_ON = createField(DSL.name("updated_on"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.TIMESTAMP)), this, "");

    private Users(Name alias, Table<UsersRecord> aliased) {
        this(alias, aliased, null);
    }

    private Users(Name alias, Table<UsersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>seedstore_db.users</code> table reference
     */
    public Users(String alias) {
        this(DSL.name(alias), USERS);
    }

    /**
     * Create an aliased <code>seedstore_db.users</code> table reference
     */
    public Users(Name alias) {
        this(alias, USERS);
    }

    /**
     * Create a <code>seedstore_db.users</code> table reference
     */
    public Users() {
        this(DSL.name("users"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : SeedstoreDb.SEEDSTORE_DB;
    }

    @Override
    public Identity<UsersRecord, Integer> getIdentity() {
        return (Identity<UsersRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<UsersRecord> getPrimaryKey() {
        return Internal.createUniqueKey(Users.USERS, DSL.name("KEY_users_PRIMARY"), new TableField[] { Users.USERS.ID }, true);
    }

    @Override
    public Users as(String alias) {
        return new Users(DSL.name(alias), this);
    }

    @Override
    public Users as(Name alias) {
        return new Users(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Users rename(String name) {
        return new Users(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Users rename(Name name) {
        return new Users(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<Integer, String, String, String, byte[], UsersUserType, Timestamp, Timestamp, Timestamp> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
    // @formatter:on
}
