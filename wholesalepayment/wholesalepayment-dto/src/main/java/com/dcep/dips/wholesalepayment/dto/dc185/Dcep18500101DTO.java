package com.dcep.dips.wholesalepayment.dto.dc185;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * dcep.185.001.01DTO
 * @Author luteng
 * @date 2025-10-12 16:17:13
 */
@Data
@Gateway(msgTp = "dcep.185.001.01", channel = @Channel(classname = ChannelEnums.AGENCY_PROCESS))
@JacksonXmlRootElement(
        localName = "BkToCstmrDbtCdtNtfctn",
        namespace = "http://www.dcep.com/dcep/18500101/"
)
public class Dcep18500101DTO extends GwDTO implements RecordDTO {
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
     * 组件Notification
     */
    @JacksonXmlProperty(
            localName = "Ntfctn"
    )
    @NotNull
    @Valid
    private Ntfctn ntfctn;

    /**
     * 组件SupplementaryData
     */
    @JacksonXmlProperty(
            localName = "SplmtryData"
    )
    @Valid
    private SplmtryData splmtryData;

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
        return MsgTpEnum.FUND_ADJUST_NOTICE.getCode();
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
