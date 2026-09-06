package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check305BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author : maxinyu
 * @version : Check305Biz.java v 0.1 2022-10-18
 * Copyright 2022 PBCDCI ALL Rights
 * @description :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = Check305BizValidator.class)
public @interface Check305Biz {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
