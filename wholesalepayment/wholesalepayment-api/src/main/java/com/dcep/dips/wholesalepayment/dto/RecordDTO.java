package com.dcep.dips.wholesalepayment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface RecordDTO {

    String encode();
    RecordDTO decode(String encode);

    @JsonIgnore
    String recMsgId();
    @JsonIgnore
    String recMsgTp();
    @JsonIgnore
    String recOrgnlMsgId();
    @JsonIgnore
    String recOrgnlMsgTp();

    /**
     * 营销信息
     */
    default PrmtInf clrPrmtInf(){
        return null;
    };

    /**
     * 收款人名称
     */
    default String clrCdtrNm(){
        return null;
    };

    /**
     * 居民类型
     */
    default String clrResdtTp(){
        return null;
    };

    /**
     * 常驻国家/地区代码
     */
    default String clrResdtCtryCd(){
        return null;
    };

    /**
     * 钱包注册手机号所在国家/地区代码
     */
    default String clrRegrCtryCd(){
        return null;
    };

    default String clrBizPayMtd(){
        return null;
    };

    default String clrRdrctUrl(){
        return null;
    };
}
