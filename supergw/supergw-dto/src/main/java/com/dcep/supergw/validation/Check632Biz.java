package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check632BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check632BizValidator.class)
public @interface Check632Biz {
    String message() default "交易状态列表长度应小于100";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
