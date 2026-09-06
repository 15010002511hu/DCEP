package com.dcep.dips.wholesalepayment.enums;

public enum ClearingPrcCdEnum {

                               BUSI_SUCCESS("DCEPI0000", "成功"),
                               
                               BUSI_PSM_CODE("DCEPI0005","推定专用码"),

                               BUSI_REJT("DCEPO6008", "应答报文返回业务拒绝");
    
                                

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
    private ClearingPrcCdEnum(String code, String description) {
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
