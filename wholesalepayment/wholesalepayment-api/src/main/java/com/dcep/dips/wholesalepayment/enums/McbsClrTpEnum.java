package com.dcep.dips.wholesalepayment.enums;

public enum McbsClrTpEnum {

    ISSUE("03", "发行"),

    REDEEM("04", "注销"),

    CROSS_CHAIN_PAY("05", "跨链支付"),

    OTHER("00", "其他");

    /** 枚举编码 */
    private final String code;

    /** 描述说明 */
    private final String description;

    /**
     * 私有构造函数。
     * 
     * @param code 枚举编码
     */
    private McbsClrTpEnum(String code, String description) {
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
