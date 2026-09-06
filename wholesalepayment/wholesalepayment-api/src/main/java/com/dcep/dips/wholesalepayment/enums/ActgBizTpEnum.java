package com.dcep.dips.wholesalepayment.enums;

/**
 * 记账业务类型枚举
 * @version $Id: BizPrtyEnum.java, v 0.1 2025年9月9日 上午10:34:33 marui Exp $
 */
public enum ActgBizTpEnum {

	SWBC01("SWBC01", "流动性调拨业务"),

	SWBC02("SWBC02", "零售净额轧差业务"),

	SWBC03("SWBC03", "批发实时结算业务"),

	SWBC04("SWBC04", "区块链服务业务"),

	SWBC05("SWBC05", "货币桥业务"),

	SWBC06("SWBC06", "JISR业务");

	/** 枚举编码 */
	private final String code;

	/** 描述说明 */
	private final String description;

	/**
	 * 私有构造函数
	 *
	 * @param code        枚举编码
	 * @param description 描述说明
	 */
	private ActgBizTpEnum(String code, String description) {
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
