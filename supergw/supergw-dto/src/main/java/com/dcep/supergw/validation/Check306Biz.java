package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check306BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @Author weiqianjing
 * @Date 2021/9/26 18:28
 * @Description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = Check306BizValidator.class)
public @interface Check306Biz {
    String message() default "当业务回执状态为“PR01”时,业务拒绝码RjctCd必填；当业务回执状态为“PR00”时,订单号OrdrNo、订单号生成时间OrdrTm、交易完成时间TrxFinishTm和交易批次号BatchId必填";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
