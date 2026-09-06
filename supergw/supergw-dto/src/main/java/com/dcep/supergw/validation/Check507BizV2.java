package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check507BizValidatorV2;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check507BizValidatorV2.class)
public @interface Check507BizV2 {
	 String message() default  "当验证类型为VT01时,授权码不填写;当验证类型为VT02,VT03,VT04时,授权码必填";

	    Class<?>[] groups() default {};

	    Class<? extends Payload>[] payload() default {};
}
