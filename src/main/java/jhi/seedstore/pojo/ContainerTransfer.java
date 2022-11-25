package jhi.seedstore.pojo;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ContainerTransfer
{
	private Integer sourceId;
	private Integer targetId;
}
