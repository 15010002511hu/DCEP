package com.dcep.dips.wholesalepayment.enums;

public enum TransTpEnum {

	NONE("", "无"),

	/** 普通转账 */
	NORMAL_TRANS("1", "普通转账"),

	/** 三方转账 */
	THIRD_PART_TRANS("2", "三方转账"),

	/** CCP转账 */
	CCP_TRANS("3", "中央对手方CCP转账");

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
	private TransTpEnum(String code, String description) {
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
