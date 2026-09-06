package com.dcep.dips.wholesalepayment.enums;

/**
 *     贷借标志
 * @author lizhiguo
 * @version $Id: ClearingProdCdtDbtIndEnum.java, v 0.1 2020年06月02日 上午10:34:33 lizhiguo Exp $
 */
public enum ClearingProdCdtDbtIndEnum {  
	/**
	 * 空
	 */
	NONE("", "空"),
	/**
	 * 贷记
	 */
	CRDT("C", "贷记"),
	/**
	 * 借记
	 */
	DBIT("D", "借记");

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
	private ClearingProdCdtDbtIndEnum(String code, String description) {
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
