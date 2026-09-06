package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check416BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = Check416BizValidator.class)
public @interface Check416Biz {
    String message() default "当QryRs为PR00时BizRpt不能为空;当为PR01时OprlErr且RjctCd不能为空)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
