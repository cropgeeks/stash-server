package jhi.seedstore.pojo;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class ContainerTransferEvent
{
	private Date    date;
	private Integer userId;
	private Integer sourceId;
	private String  sourceBarcode;
	private String  sourceType;
	private Integer targetId;
	private String  targetBarcode;
	private String  targetType;
	private Long    containerCount;
}
