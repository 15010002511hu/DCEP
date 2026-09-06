package com.dcep.dips.wholesalepayment.enums;

/**
 * 发送状态枚举
 * @author chenkai
 * @version $Id: ClearingCheckModeEnum.java, v 0.1 2019年10月18日 上午10:34:33 marui Exp $
 */
public enum ClearingSendModeEnum {
	
	INIT("0", "初始化"),

	FINISH("1", "交易终态"),

	PROCESS("2", "推送处理中"),
	
	SEND_SUCCESS("3", "推送成功");

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
	private ClearingSendModeEnum(String code, String description) {
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
