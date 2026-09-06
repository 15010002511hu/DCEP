package com.dcep.dips.wholesalepayment.enums;

/**
 * 业务检查模式枚举
 * @author chenkai
 * @version $Id: ClearingCheckModeEnum.java, v 0.1 2019年10月18日 上午10:34:33 marui Exp $
 */
public enum ClearingCheckModeEnum {
	
	NON_CHECK("-1", "不做业务检查"),

	PAYER_CHECK("0", "付款类报文检查"),

	PAYEE_CHECK("1", "收款类报文检查");

	/** 枚举编码 */
	private final String code;

	/** 描述说明 */
	private final String description;

	/**
	 * 私有构造函数。
	 * 
	 * @param code        枚举编码
	 * @param errorLevvel 错误级别
	 * @param description 描述说明
	 */
	private ClearingCheckModeEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

}
