/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.annotation;

import com.dcep.dips.wholesalepayment.enums.ActgMgmtTpEnum;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleCheckModeEnum;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface WholesalePayment {

	int confirmTimeout() default 60;

	int intervalSecond() default 7;

	int notityTimeout() default 600;

	ClearingStatusEnum presumeStatus() default ClearingStatusEnum.NONE;

	/**
	 * 业务检查模式
	 */
	WholesaleCheckModeEnum checkMode() default WholesaleCheckModeEnum.NON_CHECK;
}
