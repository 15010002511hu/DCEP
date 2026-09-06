package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check311BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author laimincai
 * @version : Check311Biz.java v 0.1 2023-01-05
 * @description : 311校验注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = Check311BizValidator.class)
public @interface Check311Biz {
	
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
