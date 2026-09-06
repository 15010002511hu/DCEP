package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check631BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check631BizValidator.class)
public @interface Check631Biz {
    String message() default "待处理交易列表长度应小于100";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
