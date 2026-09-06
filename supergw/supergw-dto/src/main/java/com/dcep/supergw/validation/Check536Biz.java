package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check536BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check536BizValidator.class)
public @interface Check536Biz {
	String message() default "当查询处理状态为PR00时,银行卡信息必填" + "当查询处理状态为PR01时,应答拒绝信息必填";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
