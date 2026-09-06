package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check434BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check434BizValidator.class)
public @interface Check434Biz {
    String message() default "业务校验不通过（当业务回执状态为“PR00成功”,原申请报文管理类型为“MT02”签约类型必须为SG01）";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
