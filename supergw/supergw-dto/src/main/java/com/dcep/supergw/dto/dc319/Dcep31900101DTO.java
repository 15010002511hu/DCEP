package com.dcep.supergw.dto.dc319;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author : maxinyu
 * @version : .java v 0.1 2023-01-04
 * Copyright 2023 PBCDCI ALL Rights
 * @description : 获取付款码请求报文 319应答320，由受理服务机构/运营机构 -> 付款运营机构
 */
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.319.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@JacksonXmlRootElement(localName = "GetPmtCodeReq", namespace = "http://www.dcep.com/dcep/31900101/")
public class Dcep31900101DTO extends GwDTO implements Serializable {
    private static final long serialVersionUID = -6510208786780402830L;

    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull
    @Valid
    GrpHdr grpHdr;
    @JacksonXmlProperty(localName = "PmtChnlInf")
    @NotNull
    @Valid
    PmtChnlInf pmtChnlInf;
    @JacksonXmlProperty(localName = "PtcInf")
    @NotNull
    @Valid
    PtcInf ptcInf;


    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        return CheckUtils.requestMsgChk(header, grpHdr);
    }
}
