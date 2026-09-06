package com.dcep.dips.wholesalepayment.enums;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;

/**
 * 结算钱包记账状态
 */
public enum ActgStsEnum {

    SUCCESS("0", "成功"),

    FAILED("1", "失败"),

    PROCESS("2", "处理中"),

    QUEUED("3", "已排队"),

    QUEUE_CANCELED("4", "排队已取消"),

    QUEUE_RETURNED("5", "排队已退回"),

    REVERSAL("6", "已抹账");

    /**
     * 枚举编码
     */
    private final String code;

    /**
     * 描述说明
     */
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code        枚举编码
     * @param description 描述说明
     */
    private ActgStsEnum(String code, String description) {
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

    public static ActgStsEnum getEnum(String code) {
        for (ActgStsEnum en : ActgStsEnum.values()) {
            if (en.getCode().equals(code)) {
                return en;
            }
        }
        throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "未知的结算钱包记账状态");
    }
}
