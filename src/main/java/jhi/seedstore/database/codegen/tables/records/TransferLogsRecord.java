/*
 * This file is generated by jOOQ.
 */
package jhi.seedstore.database.codegen.tables.records;


import java.sql.Timestamp;

import jhi.seedstore.database.codegen.tables.TransferLogs;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


// @formatter:off
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TransferLogsRecord extends UpdatableRecordImpl<TransferLogsRecord> implements Record7<String, Integer, Integer, Integer, Integer, Timestamp, Timestamp> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>seedstore_db.transfer_logs.transfer_event_id</code>.
     */
    public void setTransferEventId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>seedstore_db.transfer_logs.transfer_event_id</code>.
     */
    public String getTransferEventId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>seedstore_db.transfer_logs.container_id</code>.
     */
    public void setContainerId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>seedstore_db.transfer_logs.container_id</code>.
     */
    public Integer getContainerId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>seedstore_db.transfer_logs.source_id</code>.
     */
    public void setSourceId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>seedstore_db.transfer_logs.source_id</code>.
     */
    public Integer getSourceId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>seedstore_db.transfer_logs.target_id</code>.
     */
    public void setTargetId(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>seedstore_db.transfer_logs.target_id</code>.
     */
    public Integer getTargetId() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>seedstore_db.transfer_logs.user_id</code>.
     */
    public void setUserId(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>seedstore_db.transfer_logs.user_id</code>.
     */
    public Integer getUserId() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>seedstore_db.transfer_logs.created_on</code>.
     */
    public void setCreatedOn(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>seedstore_db.transfer_logs.created_on</code>.
     */
    public Timestamp getCreatedOn() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>seedstore_db.transfer_logs.updated_on</code>.
     */
    public void setUpdatedOn(Timestamp value) {
        set(6, value);
    }

    /**
     * Getter for <code>seedstore_db.transfer_logs.updated_on</code>.
     */
    public Timestamp getUpdatedOn() {
        return (Timestamp) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, Integer> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<String, Integer, Integer, Integer, Integer, Timestamp, Timestamp> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<String, Integer, Integer, Integer, Integer, Timestamp, Timestamp> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return TransferLogs.TRANSFER_LOGS.TRANSFER_EVENT_ID;
    }

    @Override
    public Field<Integer> field2() {
        return TransferLogs.TRANSFER_LOGS.CONTAINER_ID;
    }

    @Override
    public Field<Integer> field3() {
        return TransferLogs.TRANSFER_LOGS.SOURCE_ID;
    }

    @Override
    public Field<Integer> field4() {
        return TransferLogs.TRANSFER_LOGS.TARGET_ID;
    }

    @Override
    public Field<Integer> field5() {
        return TransferLogs.TRANSFER_LOGS.USER_ID;
    }

    @Override
    public Field<Timestamp> field6() {
        return TransferLogs.TRANSFER_LOGS.CREATED_ON;
    }

    @Override
    public Field<Timestamp> field7() {
        return TransferLogs.TRANSFER_LOGS.UPDATED_ON;
    }

    @Override
    public String component1() {
        return getTransferEventId();
    }

    @Override
    public Integer component2() {
        return getContainerId();
    }

    @Override
    public Integer component3() {
        return getSourceId();
    }

    @Override
    public Integer component4() {
        return getTargetId();
    }

    @Override
    public Integer component5() {
        return getUserId();
    }

    @Override
    public Timestamp component6() {
        return getCreatedOn();
    }

    @Override
    public Timestamp component7() {
        return getUpdatedOn();
    }

    @Override
    public String value1() {
        return getTransferEventId();
    }

    @Override
    public Integer value2() {
        return getContainerId();
    }

    @Override
    public Integer value3() {
        return getSourceId();
    }

    @Override
    public Integer value4() {
        return getTargetId();
    }

    @Override
    public Integer value5() {
        return getUserId();
    }

    @Override
    public Timestamp value6() {
        return getCreatedOn();
    }

    @Override
    public Timestamp value7() {
        return getUpdatedOn();
    }

    @Override
    public TransferLogsRecord value1(String value) {
        setTransferEventId(value);
        return this;
    }

    @Override
    public TransferLogsRecord value2(Integer value) {
        setContainerId(value);
        return this;
    }

    @Override
    public TransferLogsRecord value3(Integer value) {
        setSourceId(value);
        return this;
    }

    @Override
    public TransferLogsRecord value4(Integer value) {
        setTargetId(value);
        return this;
    }

    @Override
    public TransferLogsRecord value5(Integer value) {
        setUserId(value);
        return this;
    }

    @Override
    public TransferLogsRecord value6(Timestamp value) {
        setCreatedOn(value);
        return this;
    }

    @Override
    public TransferLogsRecord value7(Timestamp value) {
        setUpdatedOn(value);
        return this;
    }

    @Override
    public TransferLogsRecord values(String value1, Integer value2, Integer value3, Integer value4, Integer value5, Timestamp value6, Timestamp value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TransferLogsRecord
     */
    public TransferLogsRecord() {
        super(TransferLogs.TRANSFER_LOGS);
    }

    /**
     * Create a detached, initialised TransferLogsRecord
     */
    public TransferLogsRecord(String transferEventId, Integer containerId, Integer sourceId, Integer targetId, Integer userId, Timestamp createdOn, Timestamp updatedOn) {
        super(TransferLogs.TRANSFER_LOGS);

        setTransferEventId(transferEventId);
        setContainerId(containerId);
        setSourceId(sourceId);
        setTargetId(targetId);
        setUserId(userId);
        setCreatedOn(createdOn);
        setUpdatedOn(updatedOn);
    }

    /**
     * Create a detached, initialised TransferLogsRecord
     */
    public TransferLogsRecord(jhi.seedstore.database.codegen.tables.pojos.TransferLogs value) {
        super(TransferLogs.TRANSFER_LOGS);

        if (value != null) {
            setTransferEventId(value.getTransferEventId());
            setContainerId(value.getContainerId());
            setSourceId(value.getSourceId());
            setTargetId(value.getTargetId());
            setUserId(value.getUserId());
            setCreatedOn(value.getCreatedOn());
            setUpdatedOn(value.getUpdatedOn());
        }
    }
    // @formatter:on
}
