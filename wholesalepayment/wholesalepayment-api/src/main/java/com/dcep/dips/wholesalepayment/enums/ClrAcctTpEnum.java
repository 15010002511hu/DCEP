package com.dcep.dips.wholesalepayment.enums;

public enum ClrAcctTpEnum {

	NONE("", "无"),

	/** 0 钱包 <-> 钱包  */
	PAY("0", "支付"),

	/** 1 兑出：银行卡 -> 钱包  发起方：钱包方  接收方：银行方 */
	CASH_OUT("1", "兑出"),

	/** 2 兑回：钱包 -> 银行卡  发起方：钱包方  接收方：银行方 */
	CASH_IN("2", "兑回"),

	/** 3 汇款兑出 银行卡 -> 钱包   发起方：银行方  接收方：钱包方*/
	CDT_COV("3", "汇款兑出"),

	/** 4 上链 钱包 -> 区块链   发起方：钱包方  接收方：区块链*/
	CHAIN_UP("4", "上链"),
    
    /** 5 下链 钱包 -> 区块链   发起方：钱包方  接收方：区块链*/
	CHAIN_DOWN("5", "下链");

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
	private ClrAcctTpEnum(String code, String description) {
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
