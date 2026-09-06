/**
 * 
 */
package com.dcep.dips.wholesalepayment.validation;

import com.dcep.dips.wholesalepayment.validation.validator.CheckClrBatIdValidaor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenxf
 * @description : 交易转接报文批次号BatId校验
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = CheckClrBatIdValidaor.class)
public @interface CheckClrBatId {
	String message() default "交易批次号与业务处理时间不匹配";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
