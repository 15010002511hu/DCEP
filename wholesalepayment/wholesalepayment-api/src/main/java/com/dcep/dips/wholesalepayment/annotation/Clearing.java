/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.annotation;

import com.dcep.dips.wholesalepayment.enums.*;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 清算注解
 * 
 * @author liuzhenli
 * @version $Id: Clearing.java, v 0.1 2019年8月23日 下午9:41:43 liuzhenli Exp $
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface Clearing { 

	/**
	 * 超时时间：秒
	 * 
	 */
	int confirmTimeout() default 60;

	/**
	 *  间隔时间：秒
	 * 
	 */
	int intervalSecond() default 7;

	/**
	 * 通知超时时间
	 * 
	 * @return
	 */
	int notityTimeout() default 600;

	/**
	 * 推定业务状态：PR03推定成功orPR04推定失败
	 * 
	 */
	ClearingStatusEnum presumeStatus() default ClearingStatusEnum.NONE;

	/**
	 * 清算行为：prepare待清算 finish报清算 direct直接清算
	 * 
	 */
	ClearingActionEnum action() default ClearingActionEnum.NONE;

	/**
	 * 清算产品（清算模式）：0批量、1实时or 轧差
	 * 
	 */
	ClearingProdEnum clrProd() default ClearingProdEnum.REALTIME;

	InstgDrctPtyEnum instgDrctPty() default InstgDrctPtyEnum.NONE;

	/**
	 * 业务检查模式
	 */
	ClearingCheckModeEnum checkMode() default ClearingCheckModeEnum.NON_CHECK;
	
	ClearingSendModeEnum sendMode() default ClearingSendModeEnum.INIT;
	/**
	 *   贷借标志
	 *     贷记： 201、221、227、255、281、801 
	 *     借记：211、225、251
	 * @return
	 */
	ClearingProdCdtDbtIndEnum cdtDbtInd() default ClearingProdCdtDbtIndEnum.NONE;

	// Map<String,Object> attributes default new HashMap<String,Object>();
	ActgBizTpEnum actgBizTp() default ActgBizTpEnum.SWBC03;
}
