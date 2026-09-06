package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check314bizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Constraint(validatedBy = {Check314bizValidator.class})
public @interface Check314Biz {
    String message() default "314业务检查不过";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
