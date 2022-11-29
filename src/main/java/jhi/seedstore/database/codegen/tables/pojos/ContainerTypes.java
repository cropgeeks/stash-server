/*
 * This file is generated by jOOQ.
 */
package jhi.seedstore.database.codegen.tables.pojos;


import java.io.Serializable;
import java.sql.Timestamp;


import lombok.*;
import lombok.experimental.Accessors;

// @formatter:off
/**
 * This class is generated by jOOQ.
 */
@Getter
@Setter
@Accessors(chain = true)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ContainerTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer   id;
    private String    name;
    private String    description;
    private Timestamp createdOn;
    private Timestamp updatedOn;

    public ContainerTypes() {}

    public ContainerTypes(ContainerTypes value) {
        this.id = value.id;
        this.name = value.name;
        this.description = value.description;
        this.createdOn = value.createdOn;
        this.updatedOn = value.updatedOn;
    }

    public ContainerTypes(
        Integer   id,
        String    name,
        String    description,
        Timestamp createdOn,
        Timestamp updatedOn
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ContainerTypes (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(description);
        sb.append(", ").append(createdOn);
        sb.append(", ").append(updatedOn);

        sb.append(")");
        return sb.toString();
    }
    // @formatter:on
}
