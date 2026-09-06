package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check433BizValidator1;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check433BizValidator1.class)
public @interface Check433Biz1 {
    String message() default "当管理类型为MT03且签约类型为SG00时，挂接协议号必填";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
