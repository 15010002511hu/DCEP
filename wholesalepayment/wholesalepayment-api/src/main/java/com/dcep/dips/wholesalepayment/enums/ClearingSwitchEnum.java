/**
 * 
 */
package com.dcep.dips.wholesalepayment.enums;

/**
 * @author wangjie 开关枚举类
 *
 */
public enum ClearingSwitchEnum {

	OPEN("0", "开"),

	CLOSE("1", "关");
	/** 枚举编码 */
	private final String code;

	/** 描述说明 */
	private final String description;

	/**
	 * 私有构造函数。
	 * 
	 * @param code 枚举编码
	 */
	private ClearingSwitchEnum(String code, String description) {
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
