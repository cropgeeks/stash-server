package jhi.seedstore.pojo;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Sebastian Raubach
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoginDetails
{
	private String username;
	private String password;
}
