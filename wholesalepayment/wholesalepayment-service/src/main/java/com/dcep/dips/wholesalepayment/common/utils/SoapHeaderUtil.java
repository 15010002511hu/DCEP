package com.dcep.dips.wholesalepayment.common.utils;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 分区ID截取
 * 
 * @author sunxiaofeng
 * @version $Id: PartitionUtil.java, v 0.1 2019年8月27日 下午4:35:29 sunxiaofeng Exp
 *          $
 */
@Slf4j
public class SoapHeaderUtil {
	//组装报文头工具类
	public static SoapHeader createSoapHeader(String msgTp, String msgSn, String sender, String receiver) {
		try {
			SoapHeader header = new SoapHeader(Constant.SOAP_VERSION, DcepDateUtils.getDcepDateStrNow(),
					msgTp, msgSn, sender, receiver);
			header.setReceiverLEI(InfoCacheUtil.getLeiCode(receiver));
			header.setSenderLEI(InfoCacheUtil.getLeiCode(sender));
			return header;
		} catch (Exception e) {
			log.error("createSoapHeader error", e);
			throw new DcepException(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
					WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
		}
	}
}
