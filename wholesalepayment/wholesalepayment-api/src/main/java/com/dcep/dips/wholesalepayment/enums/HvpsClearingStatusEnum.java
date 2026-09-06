package com.dcep.dips.wholesalepayment.enums;

/**
 * 大额系统-业务状态
 */
public enum HvpsClearingStatusEnum {

    FORWARDED("PR00", "已转发"),

    WAIT_AUTH("PR01", "待认证"),

    PAID("PR02", "已付款"),

    NETTED("PR03", "已轧差"),

    CLEARED("PR04", "已清算"),

    SUCCEED("PR05", "已成功"),

    PROCESS("PR06", "待处理"),

    PROCESSED("PR07", "已处理"),

    CANCELLED("PR08", "已撤销"),

    REJECTED("PR09", "已拒绝"),

    ACCEPTED("PR10", "已确认"),

    NET_QUEUED("PR11", "轧差排队"),

    CLEAR_QUEUED("PR12", "清算排队"),

    CLEAR_ABNORMAL("PR13", "清算异常，待重新清算"),

    FREEZE_WAIT_CLEAR("PR16", "已冻结待清算"),

    TRANSFER_BACK("PR17", "已划回"),

    RETURNED("PR18", "已退回"),

    PAYMENT_STOP("PR21", "已止付"),

    REVERSED("PR22", "已冲正"),

    WHOLE_RETURN("PR23", "已整包退回"),

    NPC_NO_ACCEPT("PR24", "NPC未受理"),

    PART_RETURN("PR25", "已部分退回"),

    OVERDUE_RETURN("PR32", "已超期（逾期退回）"),

    FORCE_DECR_WAIT_PROCESS("PR39", "强制调减待处理");

    /** 枚举编码  */
    private final String code;

    /** 描述说明 */
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code 枚举编码
     * @param description 描述说明
     */
    private HvpsClearingStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static HvpsClearingStatusEnum getEnum(String code) {
        for (HvpsClearingStatusEnum en : HvpsClearingStatusEnum.values()) {
            if (en.getCode().equals(code)) {
                return en;
            }
        }
        return null;
    }

    public static String getDesc(String code) {
        for (HvpsClearingStatusEnum en : HvpsClearingStatusEnum.values()) {
            if (en.getCode().equals(code)) {
                return en.getDescription();
            }
        }
        return null;
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
