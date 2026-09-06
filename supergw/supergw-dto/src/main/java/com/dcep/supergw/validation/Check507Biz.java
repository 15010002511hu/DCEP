package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check507BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check507BizValidator.class)
public @interface Check507Biz {
    String message() default  "当管理类型为MT01时，签约协议号、动态关联码、验证类型、动态验证码、WalletShrtId钱包ID辩识码不填写。" + 
    		"当管理类型为MT02时，签约协议号、动态关联码、验证类型、动态验证码、WalletShrtId钱包ID辩识码必填。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
