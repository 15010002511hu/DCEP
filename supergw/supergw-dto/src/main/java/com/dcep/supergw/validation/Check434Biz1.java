package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check434BizValidator1;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check434BizValidator1.class)
public @interface Check434Biz1 {
    String message() default "业务校验不通过（当业务回执状态为“PR00”且原申请报文管理类型为“MTO2”和签约类型为“SG01”时挂接协议号必填）";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
