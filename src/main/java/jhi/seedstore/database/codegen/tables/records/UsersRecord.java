/*
 * This file is generated by jOOQ.
 */
package jhi.seedstore.database.codegen.tables.records;


import java.sql.Timestamp;

import jhi.seedstore.database.codegen.enums.UsersUserType;
import jhi.seedstore.database.codegen.tables.Users;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


// @formatter:off
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UsersRecord extends UpdatableRecordImpl<UsersRecord> implements Record9<Integer, String, String, String, byte[], UsersUserType, Timestamp, Timestamp, Timestamp> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>seedstore_db.users.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>seedstore_db.users.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>seedstore_db.users.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>seedstore_db.users.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>seedstore_db.users.email_address</code>.
     */
    public void setEmailAddress(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>seedstore_db.users.email_address</code>.
     */
    public String getEmailAddress() {
        return (String) get(2);
    }

    /**
     * Setter for <code>seedstore_db.users.password_hash</code>.
     */
    public void setPasswordHash(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>seedstore_db.users.password_hash</code>.
     */
    public String getPasswordHash() {
        return (String) get(3);
    }

    /**
     * Setter for <code>seedstore_db.users.icon</code>.
     */
    public void setIcon(byte[] value) {
        set(4, value);
    }

    /**
     * Getter for <code>seedstore_db.users.icon</code>.
     */
    public byte[] getIcon() {
        return (byte[]) get(4);
    }

    /**
     * Setter for <code>seedstore_db.users.user_type</code>.
     */
    public void setUserType(UsersUserType value) {
        set(5, value);
    }

    /**
     * Getter for <code>seedstore_db.users.user_type</code>.
     */
    public UsersUserType getUserType() {
        return (UsersUserType) get(5);
    }

    /**
     * Setter for <code>seedstore_db.users.last_login</code>.
     */
    public void setLastLogin(Timestamp value) {
        set(6, value);
    }

    /**
     * Getter for <code>seedstore_db.users.last_login</code>.
     */
    public Timestamp getLastLogin() {
        return (Timestamp) get(6);
    }

    /**
     * Setter for <code>seedstore_db.users.created_on</code>.
     */
    public void setCreatedOn(Timestamp value) {
        set(7, value);
    }

    /**
     * Getter for <code>seedstore_db.users.created_on</code>.
     */
    public Timestamp getCreatedOn() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>seedstore_db.users.updated_on</code>.
     */
    public void setUpdatedOn(Timestamp value) {
        set(8, value);
    }

    /**
     * Getter for <code>seedstore_db.users.updated_on</code>.
     */
    public Timestamp getUpdatedOn() {
        return (Timestamp) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<Integer, String, String, String, byte[], UsersUserType, Timestamp, Timestamp, Timestamp> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<Integer, String, String, String, byte[], UsersUserType, Timestamp, Timestamp, Timestamp> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Users.USERS.ID;
    }

    @Override
    public Field<String> field2() {
        return Users.USERS.NAME;
    }

    @Override
    public Field<String> field3() {
        return Users.USERS.EMAIL_ADDRESS;
    }

    @Override
    public Field<String> field4() {
        return Users.USERS.PASSWORD_HASH;
    }

    @Override
    public Field<byte[]> field5() {
        return Users.USERS.ICON;
    }

    @Override
    public Field<UsersUserType> field6() {
        return Users.USERS.USER_TYPE;
    }

    @Override
    public Field<Timestamp> field7() {
        return Users.USERS.LAST_LOGIN;
    }

    @Override
    public Field<Timestamp> field8() {
        return Users.USERS.CREATED_ON;
    }

    @Override
    public Field<Timestamp> field9() {
        return Users.USERS.UPDATED_ON;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public String component3() {
        return getEmailAddress();
    }

    @Override
    public String component4() {
        return getPasswordHash();
    }

    @Override
    public byte[] component5() {
        return getIcon();
    }

    @Override
    public UsersUserType component6() {
        return getUserType();
    }

    @Override
    public Timestamp component7() {
        return getLastLogin();
    }

    @Override
    public Timestamp component8() {
        return getCreatedOn();
    }

    @Override
    public Timestamp component9() {
        return getUpdatedOn();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public String value3() {
        return getEmailAddress();
    }

    @Override
    public String value4() {
        return getPasswordHash();
    }

    @Override
    public byte[] value5() {
        return getIcon();
    }

    @Override
    public UsersUserType value6() {
        return getUserType();
    }

    @Override
    public Timestamp value7() {
        return getLastLogin();
    }

    @Override
    public Timestamp value8() {
        return getCreatedOn();
    }

    @Override
    public Timestamp value9() {
        return getUpdatedOn();
    }

    @Override
    public UsersRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public UsersRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public UsersRecord value3(String value) {
        setEmailAddress(value);
        return this;
    }

    @Override
    public UsersRecord value4(String value) {
        setPasswordHash(value);
        return this;
    }

    @Override
    public UsersRecord value5(byte[] value) {
        setIcon(value);
        return this;
    }

    @Override
    public UsersRecord value6(UsersUserType value) {
        setUserType(value);
        return this;
    }

    @Override
    public UsersRecord value7(Timestamp value) {
        setLastLogin(value);
        return this;
    }

    @Override
    public UsersRecord value8(Timestamp value) {
        setCreatedOn(value);
        return this;
    }

    @Override
    public UsersRecord value9(Timestamp value) {
        setUpdatedOn(value);
        return this;
    }

    @Override
    public UsersRecord values(Integer value1, String value2, String value3, String value4, byte[] value5, UsersUserType value6, Timestamp value7, Timestamp value8, Timestamp value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UsersRecord
     */
    public UsersRecord() {
        super(Users.USERS);
    }

    /**
     * Create a detached, initialised UsersRecord
     */
    public UsersRecord(Integer id, String name, String emailAddress, String passwordHash, byte[] icon, UsersUserType userType, Timestamp lastLogin, Timestamp createdOn, Timestamp updatedOn) {
        super(Users.USERS);

        setId(id);
        setName(name);
        setEmailAddress(emailAddress);
        setPasswordHash(passwordHash);
        setIcon(icon);
        setUserType(userType);
        setLastLogin(lastLogin);
        setCreatedOn(createdOn);
        setUpdatedOn(updatedOn);
    }

    /**
     * Create a detached, initialised UsersRecord
     */
    public UsersRecord(jhi.seedstore.database.codegen.tables.pojos.Users value) {
        super(Users.USERS);

        if (value != null) {
            setId(value.getId());
            setName(value.getName());
            setEmailAddress(value.getEmailAddress());
            setPasswordHash(value.getPasswordHash());
            setIcon(value.getIcon());
            setUserType(value.getUserType());
            setLastLogin(value.getLastLogin());
            setCreatedOn(value.getCreatedOn());
            setUpdatedOn(value.getUpdatedOn());
        }
    }
    // @formatter:on
}
