package com.dcep.dips.wholesalepayment.enums;

/**
 * 和准备金约定的注资调整类型
 */
public enum HvpsBizKindEnum {

    INNER_TRANS("02105", "行内资金调拨"),

    RETURN_BACK("02108", "退汇");

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
    private HvpsBizKindEnum(String code, String description) {
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
