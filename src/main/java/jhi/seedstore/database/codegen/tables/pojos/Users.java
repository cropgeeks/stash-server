/*
 * This file is generated by jOOQ.
 */
package jhi.seedstore.database.codegen.tables.pojos;


import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

// @formatter:off
/**
 * This class is generated by jOOQ.
 */
@Getter
@Setter
@Accessors(chain = true)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer   id;
    private String    name;
    private String    emailAddress;
    private Timestamp createdOn;
    private Timestamp updatedOn;

    public Users() {}

    public Users(Users value) {
        this.id = value.id;
        this.name = value.name;
        this.emailAddress = value.emailAddress;
        this.createdOn = value.createdOn;
        this.updatedOn = value.updatedOn;
    }

    public Users(
        Integer   id,
        String    name,
        String    emailAddress,
        Timestamp createdOn,
        Timestamp updatedOn
    ) {
        this.id = id;
        this.name = name;
        this.emailAddress = emailAddress;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Users (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(emailAddress);
        sb.append(", ").append(createdOn);
        sb.append(", ").append(updatedOn);

        sb.append(")");
        return sb.toString();
    }
    // @formatter:on
}
