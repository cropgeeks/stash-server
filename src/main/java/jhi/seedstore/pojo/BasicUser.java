package jhi.seedstore.pojo;

import jhi.seedstore.database.codegen.enums.UsersUserType;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BasicUser
{
	private Integer       id;
	private String        name;
	private String        emailAddress;
	private String        password;
	private UsersUserType userType;
}
