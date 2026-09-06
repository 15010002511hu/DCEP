//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.dcep.dips.wholesalepayment.enums;

public enum TransTypeEnum {
    CONTRACT_CALL(0, "合约调用"),
    ORACLE_CALL(1, "预言机调用"),
    TRANSFER_MONEY(2, "转钱"),
    MAIN_SCAN(3, "主扫"),
    PASSIVE_SCAN(4, "被扫"),
    QUICK_PAY(5, "钱包快付"),
    REFUND(6, "退款"),
    REMITTANCE_OUT(7, "汇款兑出"),
    CONTRACT_SIGN(8, "合约签约"),
    CONTRACT_UPDATE(9, "合约变更"),
    CONTRACT_TERMINATE(10, "合约解约"),
    CONTRACT_UPGRADE(11, "合约升级"),
    PRODUCT_CREATE(12, "产品创建"),
    PRODUCT_VERSION_CREATE(13, "产品版本创建");

    private final int code;
    private final String desc;

    private TransTypeEnum(final int code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
