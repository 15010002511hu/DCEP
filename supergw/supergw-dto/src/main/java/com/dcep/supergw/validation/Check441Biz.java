package com.dcep.supergw.validation;

import com.dcep.supergw.validation.validator.Check441BizValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Check441BizValidator.class)
public @interface Check441Biz {
    String message() default "运营机构发起解约”，管理类型填写“MT03-解约申请”时，“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、“单位名称”、“单位证明文件类型”、“单位证明文件号码”、“法定代表人或单位负责人姓名”、“法定代表人或单位负责人证件类型” 、“法定代表人或单位负责人证件号码” 、“单位联系手机号码”等域必须填写。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
