package jhi.seedstore.pojo;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UserPasswordUpdate
{
	private String oldPassword;
	private String newPassword;
}
