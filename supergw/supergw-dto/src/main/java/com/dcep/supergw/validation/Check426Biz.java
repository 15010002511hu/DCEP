package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check426BizValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 422报文校验
 *
 * @author qinchaoyong
 * @date 2025/03/17 17:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Constraint(validatedBy = {Check426BizValidator.class})
public @interface Check426Biz {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
