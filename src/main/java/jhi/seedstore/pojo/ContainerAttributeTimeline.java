package jhi.seedstore.pojo;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ContainerAttributeTimeline
{
	private String date;
	private Map<Integer, String> attributeValues;
}
