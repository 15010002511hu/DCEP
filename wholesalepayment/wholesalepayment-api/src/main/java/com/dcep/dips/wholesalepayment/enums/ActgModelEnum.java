package com.dcep.dips.wholesalepayment.enums;

/**
 * 记账模式
 * @version $Id: BizPrtyEnum.java, v 0.1 2025年9月9日 上午10:34:33 marui Exp $
 */
public enum ActgModelEnum {

	REAL_TIME("1", "实时记账");

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
	private ActgModelEnum(String code, String description) {
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
