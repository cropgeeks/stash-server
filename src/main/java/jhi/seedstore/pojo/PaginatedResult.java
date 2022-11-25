package jhi.seedstore.pojo;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class PaginatedResult<T> implements Serializable
{
	private T    data;
	private long count = 0L;
}
