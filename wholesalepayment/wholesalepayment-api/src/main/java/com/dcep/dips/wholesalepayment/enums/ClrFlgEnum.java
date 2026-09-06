package com.dcep.dips.wholesalepayment.enums;

public enum ClrFlgEnum {

    NO("0", "不对账"),

    YES("1", "对账");

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
    private ClrFlgEnum(String code, String description) {
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

    public static ClrFlgEnum getEnum(String code) {
        for (ClrFlgEnum en : ClrFlgEnum.values()) {
            if (en.getCode().equals(code)) {
                return en;
            }
        }
        return null;
    }
}
