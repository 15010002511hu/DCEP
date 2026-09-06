package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check508BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check508BizValidator.class)
public @interface Check508Biz {
    String message() default "当管理类型为MT01且业务回执状态为PR00时填写动态关联码，用于关联身份认证和身份确认的唯一标识";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
