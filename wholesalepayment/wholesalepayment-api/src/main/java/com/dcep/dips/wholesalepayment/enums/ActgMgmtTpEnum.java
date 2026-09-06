package com.dcep.dips.wholesalepayment.enums;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;

/**
 * 记账管理类型枚举
 * @version $Id: ActgMgmtTpEnum.java, v 0.1 2025年9月9日 上午10:34:33 marui Exp $
 */
public enum ActgMgmtTpEnum {
	CAP_INJECT_INCR("00", "注资调增"),

	CAP_INJECT_DECR("01", "注资调减"),

	PRE_INJECT_INCR("10", "预注资调增"),

	PRE_INJECT_DECR("11", "预注资调减"),

	INBOUND("20", "入库"),

	OUTBOUND("21", "出库"),

	ZERO_OUT_DECR("31", "清零"),

	NORMAL("40", "正常记账"),

	FREEZE("41", "冻结记账"),

	UNFREEZE_ACTG("42", "解冻并记账");

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
	private ActgMgmtTpEnum(String code, String description) {
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

	public static ActgMgmtTpEnum getEnum(String code) {
		for (ActgMgmtTpEnum en : ActgMgmtTpEnum.values()) {
			if (en.getCode().equals(code)) {
				return en;
			}
		}
		throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "未知的记账管理类型");
	}

	public static String getDesc(String code) {
		for (ActgMgmtTpEnum en : ActgMgmtTpEnum.values()) {
			if (en.getCode().equals(code)) {
				return en.getDescription();
			}
		}
		throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "未知的记账管理类型");
	}
}
