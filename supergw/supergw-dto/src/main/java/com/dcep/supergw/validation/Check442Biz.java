package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check442BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check442BizValidator.class)
public @interface Check442Biz {
    String message() default "当管理类型为“MT06”，且业务回执状态为“PR00”时ChnlInf必填写";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
