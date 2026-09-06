package com.dcep.dips.wholesalepayment.enums;

/**
 * 清算状态枚举
 */
public enum ClearingActionEnum {

	NONE("", "无状态"),

	PREPARE("0", "待清算"),

	FINISH("1", "报清算"),

	DIRECT("2", "直接清算");

	/** 枚举编码 */
	private final String code;

	/** 描述说明 */
	private final String description;

	/**
	 * 私有构造函数。
	 * 
	 * @param code        枚举编码
	 * @param description 描述说明
	 */
	private ClearingActionEnum(String code, String description) {
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
