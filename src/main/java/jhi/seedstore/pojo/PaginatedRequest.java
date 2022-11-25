package jhi.seedstore.pojo;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Sebastian Raubach
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class PaginatedRequest
{
	private String   orderBy;
	private Integer  ascending;
	private int      limit     = Integer.MAX_VALUE;
	private int      page      = 0;
	private long     prevCount = -1;
	private Filter[] filter;
}