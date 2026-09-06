package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check420BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check420BizValidator.class)
public @interface Check420Biz {
    String message() default "查询处理状态为PR00时需要填写应答的原业务信息";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
