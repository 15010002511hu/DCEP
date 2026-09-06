package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check320BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author : maxinyu
 * @version : Check320Biz.java v 0.1 2023-01-05
 * Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = Check320BizValidator.class)
public @interface Check320Biz {
    String message() default "320业务检查不过";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
