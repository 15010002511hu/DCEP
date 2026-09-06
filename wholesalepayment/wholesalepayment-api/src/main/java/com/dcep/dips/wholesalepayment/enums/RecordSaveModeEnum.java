package com.dcep.dips.wholesalepayment.enums;

public enum RecordSaveModeEnum {

    NONE("", "不存储"),

    ABSTRACT("0", "摘要字段"),

    ALL("1", "全文存储");

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
    private RecordSaveModeEnum(String code, String description) {
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
