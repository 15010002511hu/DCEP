package com.dcep.dips.wholesalepayment.enums;

public enum LockEnum {

	UNLOCK("0", "未锁"),

	LOCK("1", "已锁");

	/** 枚举编码 */
	private final String code;

	/** 描述说明 */
	private final String description;

	/**
	 * 私有构造函数。
	 *
	 * @param code 枚举编码
	 */
	private LockEnum(String code, String description) {
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
