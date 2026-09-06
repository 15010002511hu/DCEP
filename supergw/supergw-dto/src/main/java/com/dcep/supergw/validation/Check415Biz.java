package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check415BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = Check415BizValidator.class)
public @interface Check415Biz {
    String message() default "当交易类型为“TT03”时，收款运营机构需要校验订单号、商户号、订单金额的真实性和有效性；当交易类型为TT00、TT01、TT02时填写收款码信息";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
