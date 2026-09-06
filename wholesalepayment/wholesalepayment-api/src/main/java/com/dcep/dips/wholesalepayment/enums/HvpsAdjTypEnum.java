package com.dcep.dips.wholesalepayment.enums;

/**
 * 和准备金约定的注资调整类型
 */
public enum HvpsAdjTypEnum {

    INCREASE("PRFD", "注资调增"),

    DECREASE("FDRD", "注资调减"),

    PRE_INCREASE("FFIC", "预注资调增"),

    PRE_DECREASE("FFRD", "预注资调减"),

    ZERO_OUT("TRBH","清零");

    /** 准备金调整类型  */
    private final String code;

    /** 描述说明 */
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code 大额准备金交易类型
     * @param description 描述说明
     */
    private HvpsAdjTypEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static HvpsAdjTypEnum getEnum(String code) {
        for (HvpsAdjTypEnum en : HvpsAdjTypEnum.values()) {
            if (en.getCode().equals(code)) {
                return en;
            }
        }
        return null;
    }

    public static String getDesc(String code) {
        for (HvpsAdjTypEnum en : HvpsAdjTypEnum.values()) {
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
