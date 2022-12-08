package jhi.seedstore.pojo;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ContainerAttributeValue
{
	private Integer attributeId;
	private String  attributeName;
	private String  attributeValue;
}
