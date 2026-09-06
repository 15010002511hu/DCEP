package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check316bizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author : maxinyu
 * @version : Check316Biz.java v 0.1 2021-04-14
 * @description :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
@Constraint(validatedBy = {Check316bizValidator.class})
public @interface Check316Biz {
    String message() default "316业务检查不过";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
