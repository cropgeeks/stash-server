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
public class TransferLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    private String    transferEventId;
    private Integer   containerId;
    private Integer   sourceId;
    private Integer   targetId;
    private Integer   userId;
    private Timestamp createdOn;
    private Timestamp updatedOn;

    public TransferLogs() {}

    public TransferLogs(TransferLogs value) {
        this.transferEventId = value.transferEventId;
        this.containerId = value.containerId;
        this.sourceId = value.sourceId;
        this.targetId = value.targetId;
        this.userId = value.userId;
        this.createdOn = value.createdOn;
        this.updatedOn = value.updatedOn;
    }

    public TransferLogs(
        String    transferEventId,
        Integer   containerId,
        Integer   sourceId,
        Integer   targetId,
        Integer   userId,
        Timestamp createdOn,
        Timestamp updatedOn
    ) {
        this.transferEventId = transferEventId;
        this.containerId = containerId;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.userId = userId;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TransferLogs (");

        sb.append(transferEventId);
        sb.append(", ").append(containerId);
        sb.append(", ").append(sourceId);
        sb.append(", ").append(targetId);
        sb.append(", ").append(userId);
        sb.append(", ").append(createdOn);
        sb.append(", ").append(updatedOn);

        sb.append(")");
        return sb.toString();
    }
    // @formatter:on
}
