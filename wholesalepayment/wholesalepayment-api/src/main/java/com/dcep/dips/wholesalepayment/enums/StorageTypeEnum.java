/**
 * 
 */
package com.dcep.dips.wholesalepayment.enums;

/**
 * @author wangjie 开关枚举类
 *
 */
public enum StorageTypeEnum {

	INST_ASYNC_NOTICE("0", "动账消息推送"),//动账消息推送、机构异步通知模型一致，进行了合并。

	ACTG_ADJUST_DECREASE("2", "结算钱包资金调减重发"),

	HVPS_ADJUST_DECREASE("3", "大额资金调减重发"),

	HVPS_ZERO_OUT("4", "大额资金清零重发"),

	MCBS_ASYNC_NOTICE("5", "货币桥异步通知");

	/** 枚举编码 */
	private final String code;

	/** 描述说明 */
	private final String description;

	/**
	 * 私有构造函数。
	 *
	 * @param code 枚举编码
	 */
	private StorageTypeEnum(String code, String description) {
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
