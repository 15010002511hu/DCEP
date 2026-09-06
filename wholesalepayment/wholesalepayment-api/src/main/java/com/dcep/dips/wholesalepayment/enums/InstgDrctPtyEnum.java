package com.dcep.dips.wholesalepayment.enums;

public enum InstgDrctPtyEnum {

    NONE("", "无状态"),

    DBTR("DBTR", "付款机构"),

    CDTR("CDTR", "收款机构");

    /** 枚举编码  */
    private final String code;

    /** 描述说明 */
    private final String description;

    /**
     * 私有构造函数。
     * 
     * @param code 枚举编码 
     * @param errorLevvel 错误级别
     * @param description 描述说明
     */
    private InstgDrctPtyEnum(String code, String description) {
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
