/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.annotation;

import com.dcep.dips.wholesalepayment.enums.RecordSaveModeEnum;

import java.lang.annotation.*;

/**
 * 档案Record注解
 * @author liuzhenli
 * @version $Id: Record.java, v 0.1 2019年8月23日 下午11:37:52 liuzhenli Exp $
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface Record {
    RecordSaveModeEnum saveMode() default RecordSaveModeEnum.NONE;
}
