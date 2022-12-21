package jhi.seedstore.util;

import jakarta.ws.rs.NameBinding;
import jhi.seedstore.database.codegen.enums.UsersUserType;

import java.lang.annotation.*;

/**
 * Annotation used to secure server resources.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Secured
{
	UsersUserType[] value() default {};
}
