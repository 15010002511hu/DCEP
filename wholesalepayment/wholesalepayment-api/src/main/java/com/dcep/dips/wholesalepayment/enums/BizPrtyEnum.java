package com.dcep.dips.wholesalepayment.enums;

/**
 * 业务优先级枚举
 */
public enum BizPrtyEnum {

	NORM("NORM", "普通"),

	HIGH("HIGH", "紧急"),

	URGT("URGT", "特急");

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
	private BizPrtyEnum(String code, String description) {
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
