package com.dcep.dips.wholesalepayment.enums;

public enum ErrorEnum {

	//通知信息
	BUSI_SUCCESS("DCEPI0000", "处理成功"), 
	BUSI_PSM_CODE("DCEPI0005", "推定专用码"),
	//业务报错信息
	BUSI_REJT("DCEPO6008", "应答报文返回业务拒绝"),
	BUSI_DUPLICATION("DCEPO0001", "重复的业务"), 
	BUSI_DUPLICATION_STS("DCEPO0001", "重复的业务，交易状态不一致"), 
	BUSI_NO_ACCESS("DCEPO5008", "无权查询此业务"),
	BUSI_NO_PROCESS("DCEPO5009", "无权进行此操作"),
	BUSI_DATA_NOTMATCH("DCEPO6003", "应答报文或回执报文和原业务要素不匹配"), 
	BUSI_BIZ_NOTMATCH("DCEPO1110", "原业务种类或原业务类型不匹配"),
	BUSI_FIELD_NOTMATCH("DCEPO1012", "字段与原交易要素不匹配"), 
	BUSI_DATA_NOTEXCEED("DCEPO6001", "数据库中无指定数据"),
	RESP_NO_MATCH_ORIGINAL_BUSI("DCEPO6002", "应答报文或回执报文没有匹配的原业务"),
	SENDER_STATE_ILLEGAL("DCEPO3018", "发起运营机构没有登录"), 
	RECEIVER_STATE_ILLEGAL("DCEPO3022", "接收运营机构没有登录"),
	CSHBXINST_STATE_ILLEGAL("DCEPO3031", "钱柜运营机构没有登录"),
	CREDTTM_ILLEGAL("DCEPO1036", "报文体发送时间不在业务受理范围内"), 
	MSGID_DATE_ILLEGAL("DCEPO1037", "报文标识号不在业务受理范围内"),
	BATCHID_CRETIME_NOT_MATCH("DCEPO1038", "交易批次号与业务处理时间不匹配"),
	BIZ_KIND_ILLEGAL("DCEPO1103", "差错交易业务种类非法"),
	ORGN_MT_ILLEGAL("DCEPO1106", "原报文类型非法"),
	NO_MATCH_ORIGNAL("DCEPO0006","没有匹配的原业务"),
	DSPT_RECEIVER_ILLEGAL("DCEPO6017","业务接收运营机构与原业务发起运营机构不一致"),
	DSPT_SENDER_ILLEGAL("DCEPO6018","业务发起运营机构与原业务发起运营机构不一致"),
	MSG_CSHBOXINF_ILLEGAL("DCEPO0118","报文钱柜信息应为空"),
	BUSI_REVERSED("DCEPO6004","原业务已撤销"),
	BUSI_PRESUMED_CANNOT_PAY("DCEPO6071","交易已推定失败，不能支付"),
	BUSI_CANCELLED_CANNOT_PAY("DCEPO6072","交易已被撤销，不能支付"),
	BUSI_SUCC_CANNOT_CANCEL("DCEPO6073","交易已支付成功，不能撤销"),
	BUSI_PRESUMED_CANNOT_CANCEL("DCEPO6074","交易已推定失败，不能撤销"),
	BUSI_FAILED_CANNOT_PAY("DCEPO6075","交易已失败，不能支付"),
	BUSI_DAYEND_RETURN_CANNOT_PAY("DCEPO6076","交易已日终退回，不能支付"),
	DSPT_DATE_ILLEGAL("DCEPO1047", "差错调账原交易时间不在业务受理范围内"),
	DSPT_BIZ_NOTMATCH("DCEPO1048", "差错调账业务要素不匹配"),
	ORG_DISPUTE_SUCCESS("DCEPO1049","对原交易差错调账已成功不允许多次调账"),
	ORG_DISPUTE_ING("DCEPO1050","对原交易差错调账处理中不允许多次调账"),
	USER_NOT_MATCH_RECEIVER("DCEPO1051","接收机构与用户所属信息不一致"),
	//系统报错信息
	BUSI_COMP_ERROR("DCEPS9400", "组件调用异常"), 
	BUSI_UPDATE_DB_EXCEPTION("DCEPS5006", "修改数据库中的数据错误"),
	BUSI_DB_KEYWORD_REPEAT("DCEPS5103", "数据表关键字重复"),
	BUSI_DB_ACC_ERR("DCEPS5001", "访问数据库异常"),
	UNKNOWN_EXCEPTION("DCEPS9999", "其他系统错");



	/** 枚举编码 */
	private final String code;

	/** 描述说明 */
	private final String description;

	/**
	 * 私有构造函数
	 * 
	 * @param code        枚举编码
	 * @param description 描述说明
	 */
	private ErrorEnum(String code, String description) {
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

	public static ErrorEnum getEnum(String code) {
		for (ErrorEnum en : ErrorEnum.values()) {
			if (en.getCode().equals(code)) {
				return en;
			}
		}
		return null;
	}

}
