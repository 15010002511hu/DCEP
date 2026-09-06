package com.dcep.dips.wholesalepayment.enums;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;

/**
 * 数字人民币运营管理中心-业务状态
 */
public enum ClearingStatusEnum {

    NONE("", "无状态"),

    SUCCESS("PR00", "成功"),

    FAILED("PR01", "失败"),

    PROCESS("PR02", "处理中"),

    PRESUME_SUCCESS("PR03", "推定成功"),

    PRESUME_FAILED("PR04", "推定失败"),

    ACCEPTED("PR06", "受理成功"),

    PROCESSED("PR09", "已处理"),

    SETTLED("PR10", "已结算"),

    SETTLE_QUEUE("PR11", "结算排队"),

    WAIT_SETTLE("PR12", "待结算"),

    CANCELLED("PR13", "已撤销"),

    DAYEND_RETURN("PR14", "日终退回");

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
    private ClearingStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ClearingStatusEnum getEnum(String code) {
        for (ClearingStatusEnum en : ClearingStatusEnum.values()) {
            if (en.getCode().equals(code)) {
                return en;
            }
        }
        throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "未知的业务状态");
    }

    public static String getDesc(String code) {
        for (ClearingStatusEnum en : ClearingStatusEnum.values()) {
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
