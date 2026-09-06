/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc961;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.annotation.GwMethod;
import com.dcep.common.annotation.RpcInfo;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.OrgCache;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

@Gateway(msgTp = "dcep.961.001.01", channel = @Channel(
        classname = ChannelEnums.CENTRAL_PROCESS, 
        services = {
                @RpcInfo(name = "com.dcep.supergw.service.IndustryKeyService", methods = {@GwMethod(name = "execute" )})
        }))
@JacksonXmlRootElement(localName = "GetBizApplKeyReq", namespace = "http://www.dcep.com/dcep/96100101/")
public class Dcep96100101DTO extends GwDTO {

    private static final long serialVersionUID = -8251105376854355903L;

    /*
     * 业务头组件 
     */
    @Valid
    @NotNull
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @JacksonXmlProperty(localName = "GrpHdr")
    private GrpHdr grpHdr;
    
    /*
     * 行业信息
     */
    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "IndstryInf")
    private IndstryInf indstryInf;
    
    /*
     * 证书信息
     */
    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "CertInf")
    private CertInf certInf;

    @Override
    public void init() {
        
    }

    @Override
    public String fetchMsgId() {
        if(grpHdr != null) {
            return grpHdr.getMsgId();
        } else {
            return null;
        }
    }

    @Override
    public boolean check(SoapHeader header) {
        // 发起机构内部编码
        String senderInnerCode = OrgCache.getInstance().getFiInf(header.getSender()).getOrgInnerCode();
        // 机构分散因子机构编码
        String instnFctrIdCode = indstryInf.getInstnFctrId().substring(0, 6);
        // 检查机构分散因子前6位为发起机构内部编码
        if(!StringUtils.equals(senderInnerCode, instnFctrIdCode)) {
            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(),
                    "报文发起机构编码与机构分散因子机构编码不匹配");
        }
        
        return CheckUtils.requestMsgChk(header, grpHdr);
    }

    public GrpHdr getGrpHdr() {
        return grpHdr;
    }

    public void setGrpHdr(GrpHdr grpHdr) {
        this.grpHdr = grpHdr;
    }

    public IndstryInf getIndstryInf() {
        return indstryInf;
    }

    public void setIndstryInf(IndstryInf indstryInf) {
        this.indstryInf = indstryInf;
    }

    public CertInf getCertInf() {
        return certInf;
    }

    public void setCertInf(CertInf certInf) {
        this.certInf = certInf;
    }

    @Override
    public String toString() {
        return "Dcep96100101DTO [grpHdr=" + grpHdr + ", indstryInf=" + indstryInf + ", certInf=" + certInf + "]";
    }
}
