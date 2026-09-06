package com.dcep.supergw.dto.dc371;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.encryption.TransEncryption;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.HeaderTransfer;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author : maxinyu
 * @version : DCEP37100101DTO.java v 0.1 2021-04-14
 * @description : 对账汇总通知报文
 */
@JacksonXmlRootElement(localName = "ReconSummaryChk", namespace = "http://www.dcep.com/dcep/37100101/")
@Gateway(msgTp = "dcep.371.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@HeaderTransfer
public class DCEP37100101DTO extends GwDTO implements TransEncryption {

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "GrpHdr")
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    private GrpHdr grpHdr;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "SummaryChkInf")
    private SummaryChkInf summaryChkInf;


    public GrpHdr getGrpHdr() {
        return grpHdr;
    }

    public void setGrpHdr(GrpHdr grpHdr) {
        this.grpHdr = grpHdr;
    }

    public SummaryChkInf getSummaryChkInf() {
        return summaryChkInf;
    }

    public void setSummaryChkInf(SummaryChkInf summaryChkInf) {
        this.summaryChkInf = summaryChkInf;
    }

    @Override
    public String toString() {
        return "DCEP37100101DTO{" +
                "grpHdr=" + grpHdr +
                ", summaryChkInf=" + summaryChkInf +
                '}';
    }

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


    @Override
    public void transEncrypt(EncryptionHelper encryptionHelper) {
        encryptionHelper.encrypt("");
    }
}
