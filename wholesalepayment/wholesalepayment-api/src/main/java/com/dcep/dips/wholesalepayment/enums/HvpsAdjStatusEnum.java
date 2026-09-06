package com.dcep.dips.wholesalepayment.enums;

/**
 * 大额系统调增、调减结果
 */
public enum HvpsAdjStatusEnum {
	SUCCESS("0", "调整成功"),

	FAILED("1", "调整失败");

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
	private HvpsAdjStatusEnum(String code, String description) {
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

	@Override
	public String toString() {
		return String.format("%s(%s)", this.name(), this.getCode());
	}
}
