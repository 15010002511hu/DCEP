package com.dcep.dips.wholesalepayment.enums;

public enum AbstractEnum {
    NONE("", ""),
    TRANS("100", "转账"),
    CAP_INJECT_INCR("001001", "注资调增"),
    CAP_INJECT_DECR("001002", "注资调减"),
    PRE_INJECT_INCR("001003", "预注资调增"),
    PRE_INJECT_DECR("001004", "预注资调减"),
    ZERO_OUT_DECR("001005", "预注资调减");

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
    private AbstractEnum(String code, String description) {
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

    public static AbstractEnum getEnum(String code) {
        for (AbstractEnum en : AbstractEnum.values()) {
            if (en.getCode().equals(code)) {
                return en;
            }
        }
        return null;
    }
}
