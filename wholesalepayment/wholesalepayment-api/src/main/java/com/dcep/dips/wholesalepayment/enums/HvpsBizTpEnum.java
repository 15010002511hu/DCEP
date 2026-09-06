package com.dcep.dips.wholesalepayment.enums;

/**
 * 和准备金约定的注资调整类型
 */
public enum HvpsBizTpEnum {

    INTER_BANK_TRANS("A200", "行间资金汇划"),
    RETURN_BACK("A105", "退汇");

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
    private HvpsBizTpEnum(String code, String description) {
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
