package com.dcep.dips.wholesalepayment.enums;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;

/**
 * 清零处理状态
 */
public enum ZerooutPrcStsEnum {

    SUCCESS("00", "清零成功"),

    FAILED("01", "清零失败"),

    EXCHANGE("02", "AB账户切换中"),

    PROCESS("03", "清零中");

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
    private ZerooutPrcStsEnum(String code, String description) {
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

    public static ZerooutPrcStsEnum getEnum(String code) {
        for (ZerooutPrcStsEnum en : ZerooutPrcStsEnum.values()) {
            if (en.getCode().equals(code)) {
                return en;
            }
        }
        throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "未知的结算钱包记账状态");
    }
}
