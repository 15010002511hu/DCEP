package com.dcep.dips.wholesalepayment.enums;

public enum McbsStatusEnum {
    EPTY("EPTY", "无记录"),
    SUCD("SUCD", "成功"),
    FAIL("FAIL", "失败"),
    PDNG("PDNG", "处理中"),
    RSVL("RSVL", "已受理"),
    RJCT("RJCT", "拒绝");

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
    private McbsStatusEnum(String code, String description) {
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
