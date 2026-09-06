package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check433BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check433BizValidator.class)
public @interface Check433Biz {
    String message() default "业务校验不通过(管理类型MgmtTp为MTO3,签约类型SgnTp必须为SG00)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
