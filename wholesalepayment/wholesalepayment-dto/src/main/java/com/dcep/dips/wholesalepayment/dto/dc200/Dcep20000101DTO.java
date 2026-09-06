package com.dcep.dips.wholesalepayment.dto.dc200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * dcep.200.001.01DTO
 * @Author luteng
 * @date 2025-09-28 11:40:42
 */
@Data
@Gateway(
        msgTp = "dcep.200.001.01"
)
@JacksonXmlRootElement(
        localName = "FIToFIPmtStsRpt",
        namespace = "http://www.dcep.com/dcep/20000101/"
)
@NoArgsConstructor
@AllArgsConstructor
public class Dcep20000101DTO extends GwDTO implements RecordDTO {
    /**
     * 组件GroupHeader
     */
    @JacksonXmlProperty(
            localName = "GrpHdr"
    )
    @NotNull
    @Valid
    private GrpHdr grpHdr;

    /**
     * 组件TransactionInformationAndStatus
     */
    @JacksonXmlProperty(
            localName = "TxInfAndSts"
    )
    @NotNull
    @Valid
    private TxInfAndSts txInfAndSts;

    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        return true;
    }

    @Override
    public String encode() {
        return JSON.toJSONString(this);
    }

    @Override
    public RecordDTO decode(String encode) {
        return JSONObject.toJavaObject(JSON.parseObject(encode), this.getClass());
    }

    @Override
    public String recMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public String recMsgTp() {
        return MsgTpEnum.SETTLE_NOTICE.getCode();
    }

    @Override
    public String recOrgnlMsgId() {
        return "";
    }

    @Override
    public String recOrgnlMsgTp() {
        return "";
    }
}
