package com.dcep.dips.wholesalepayment.enums;

public enum WholesaleErrorEnum {

    // 通知信息
    BUSI_SUCCESS("DCEPI0000", "处理成功"),
    BUSI_PSM_CODE("DCEPI0005", "推定专用码"),

    //业务报错信息
    BUSI_DUPLICATION("DCEPO0001", "重复的业务"),
    BUSI_DUPLICATION_STS("DCEPO0001", "重复的业务，交易状态不一致"),
    NO_MATCH_ORIGNAL("DCEPO0006","没有匹配的原业务"),
    BUSI_REJT("DCEPO6008", "应答报文返回业务拒绝"),
    BUSI_NOT_SUPPORTED("DCEPO6009", "不支持的业务类型"),
    MSGTP_NOT_SUPPORTED("DCEPO6010", "不支持的报文编号"),
    BIZSTS_NOT_SUPPORTED("DCEPO6011", "不支持的业务状态"),
    CREDTTM_ILLEGAL("DCEPO1036", "报文体发送时间不在业务受理范围内"),
    MSGID_DATE_ILLEGAL("DCEPO1037", "报文标识号不在业务受理范围内"),
    DBTR_STATE_ILLEGAL("DCEPO3006", "付款参与机构没有登录"),
    CDTR_STATE_ILLEGAL("DCEPO3013", "收款参与机构没有登录"),
    SYSTEM_STATE_ILLEGAL("DCEPO3017", "系统非正常状态"),
    SENDER_STATE_ILLEGAL("DCEPO3018", "发起运营机构没有登录"),
    RECEIVER_STATE_ILLEGAL("DCEPO3022", "接收运营机构没有登录"),
    ACCT_REJECT("DCEPO3023", "结算钱包返回失败"),
    HVPS_REJECT("DCEPO3024", "HVPS返回失败"),
    HVPS_DISORDER("DCEPO3025", "HVPS状态乱序"),

    HEADER_BODY_SEND_PTY_ID_NOT_MATCH_ERROR("DCEPO3060", "报文头发起机构与报文体发起参与机构不相等"),
    BODY_SEND_PTY_ID_NOT_MATCH_ERROR("DCEPO3061", "报文体中发起参与机构与被调减参与机构不相等"),
    BODY_RECEIVE_PTY_ID_NOT_MATCH_ERROR("DCEPO3062", "报文头中接收机构不是运营中心"),
    BODY_SEND_PTY_ID_PRE_NOT_MATCH_ERROR("DCEPO3063", "报文体中发起参与机构与被预注资调减参与机构不相等"),
    AMOUNT_ERR("DCEPO3064", "金额转换失败"),



    // 系统报错信息
    BUSI_COMP_ERROR("DCEPS9400", "组件调用异常"),
    BUSI_UPDATE_DB_EXCEPTION("DCEPS5006", "修改数据库中的数据错误"),
    BUSI_DB_KEYWORD_REPEAT("DCEPS5103", "数据表关键字重复"),
    BUSI_DB_ACC_ERR("DCEPS5001", "访问数据库异常"),
    UNKNOWN_EXCEPTION("DCEPS9999", "其他系统错"),

    BUSI_COMP_TIMEOUT("DCEPS9401", "业务处理超时置为失败"); //TODO 能否加

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
    private WholesaleErrorEnum(String code, String description) {
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

    public static WholesaleErrorEnum getEnum(String code) {
        for (WholesaleErrorEnum en : WholesaleErrorEnum.values()) {
            if (en.getCode().equals(code)) {
                return en;
            }
        }
        return null;
    }
}
