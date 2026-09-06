/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.utils;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.dips.wholesalepayment.dto.common.Common;
import org.apache.dubbo.common.utils.StringUtils;

/**
 * DTO检查工具类
 * 
 * @author chenkai
 * @version $Id: DtoCheckUtil.java, v 0.1 2019年10月18日 上午11:15:43 chenkai Exp $
 */
public class DtoCheckUtil {

	/**
	 * 检查付款报文的报文体运营机构是否匹配
	 * 
	 * @param sender
	 * @param payer
	 * @param receiver
	 * @param payee
	 */
	public static boolean checkPayerMsgInst(String sender, String payer, String receiver, String payee) {
	    //防止报文体中非必输情况
        if (StringUtils.isBlank(payer) || StringUtils.isBlank(payee)) {
            return true;
        }
		if (!sender.equals(payer)) {
			throw new DcepException(ErrorEnum.SENDER_NOT_MATCH_PAYER_ERROR);
		} else if (!receiver.equals(payee)) {
			throw new DcepException(ErrorEnum.RECEIVER_NOT_MATCH_PAYEE_ERROR);
		}
		return true;
	}

	/**
	 * 检查收款报文的报文体运营机构是否匹配
	 * 
	 * @param sender
	 * @param payer
	 * @param receiver
	 * @param payee
	 */
	public static boolean checkPayeeMsgInst(String sender, String payer, String receiver, String payee) {
		//防止报文体中非必输情况
		if (StringUtils.isBlank(payer) || StringUtils.isBlank(payee)) {
            return true;
		}
		if (!sender.equals(payee)) {
			throw new DcepException(ErrorEnum.SENDER_NOT_MATCH_PAYEE_ERROR);
		} else if (!receiver.equals(payer)) {
			throw new DcepException(ErrorEnum.RECEIVER_NOT_MATCH_PAYER_ERROR);
		}
		return true;
	}

	/**
	 * 校验钱包是压测钱包必须收付款方都是压测，非压测钱包收付款方都非压测
	 *
	 * @param dbtrWltId
	 * @param cdtrWltId
	 */
	public static boolean checkDbCrWltId(String dbtrWltId, String cdtrWltId) {
		return isPressWlt(dbtrWltId) == isPressWlt(cdtrWltId);
	}

	//判断是否是压测钱包
	private static boolean isPressWlt(String wltId) {
		if (StringUtils.isBlank(wltId)) {
			return false;
		}
		return Common.PRESS_END_T.equals(wltId.substring(wltId.length()-1));
	}
}
