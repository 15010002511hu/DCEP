package com.dcep.dips.wholesalepayment.enums;

public enum ClrProdEnum {

    BATCH("0", "批量清算"),

    REALTIME("1", "实时清算"),

    NONE("2", "不清算");

    private final String code;
    private final String desc;

    /***
     * 私有构造函数
     *
     * @param code        错误枚举
     * @param description 描述说明
     */
    private ClrProdEnum(String code, String desc) {

        this.code = code;
        this.desc = desc;
    }

    public String getDescription() {
        return desc;
    }

    public String getCode() {
        return code;
    }

}