/*
 * This file is generated by jOOQ.
 */
package jhi.seedstore.database.codegen.tables;


import jhi.seedstore.database.codegen.SeedstoreDb;
import jhi.seedstore.database.codegen.tables.records.TransferLogsRecord;
import org.jooq.*;
import org.jooq.impl.Internal;
import org.jooq.impl.*;

import java.sql.Timestamp;


// @formatter:off
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TransferLogs extends TableImpl<TransferLogsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>seedstore_db.transfer_logs</code>
     */
    public static final TransferLogs TRANSFER_LOGS = new TransferLogs();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TransferLogsRecord> getRecordType() {
        return TransferLogsRecord.class;
    }

    /**
     * The column <code>seedstore_db.transfer_logs.id</code>.
     */
    public final TableField<TransferLogsRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>seedstore_db.transfer_logs.container_id</code>.
     */
    public final TableField<TransferLogsRecord, Integer> CONTAINER_ID = createField(DSL.name("container_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>seedstore_db.transfer_logs.source_id</code>.
     */
    public final TableField<TransferLogsRecord, Integer> SOURCE_ID = createField(DSL.name("source_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>seedstore_db.transfer_logs.target_id</code>.
     */
    public final TableField<TransferLogsRecord, Integer> TARGET_ID = createField(DSL.name("target_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>seedstore_db.transfer_logs.user_id</code>.
     */
    public final TableField<TransferLogsRecord, Integer> USER_ID = createField(DSL.name("user_id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>seedstore_db.transfer_logs.created_on</code>.
     */
    public final TableField<TransferLogsRecord, Timestamp> CREATED_ON = createField(DSL.name("created_on"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>seedstore_db.transfer_logs.updated_on</code>.
     */
    public final TableField<TransferLogsRecord, Timestamp> UPDATED_ON = createField(DSL.name("updated_on"), SQLDataType.TIMESTAMP(0).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.TIMESTAMP)), this, "");

    private TransferLogs(Name alias, Table<TransferLogsRecord> aliased) {
        this(alias, aliased, null);
    }

    private TransferLogs(Name alias, Table<TransferLogsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>seedstore_db.transfer_logs</code> table reference
     */
    public TransferLogs(String alias) {
        this(DSL.name(alias), TRANSFER_LOGS);
    }

    /**
     * Create an aliased <code>seedstore_db.transfer_logs</code> table reference
     */
    public TransferLogs(Name alias) {
        this(alias, TRANSFER_LOGS);
    }

    /**
     * Create a <code>seedstore_db.transfer_logs</code> table reference
     */
    public TransferLogs() {
        this(DSL.name("transfer_logs"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : SeedstoreDb.SEEDSTORE_DB;
    }

    @Override
    public Identity<TransferLogsRecord, Integer> getIdentity() {
        return (Identity<TransferLogsRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<TransferLogsRecord> getPrimaryKey() {
        return Internal.createUniqueKey(TransferLogs.TRANSFER_LOGS, DSL.name("KEY_transfer_logs_PRIMARY"), new TableField[] { TransferLogs.TRANSFER_LOGS.ID }, true);
    }

    @Override
    public TransferLogs as(String alias) {
        return new TransferLogs(DSL.name(alias), this);
    }

    @Override
    public TransferLogs as(Name alias) {
        return new TransferLogs(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TransferLogs rename(String name) {
        return new TransferLogs(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TransferLogs rename(Name name) {
        return new TransferLogs(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Integer, Integer, Integer, Integer, Integer, Timestamp, Timestamp> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
    // @formatter:on
}
