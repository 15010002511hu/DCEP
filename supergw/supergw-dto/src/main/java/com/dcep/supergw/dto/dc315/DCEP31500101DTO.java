package com.dcep.supergw.dto.dc315;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author : maxinyu
 * @version : DCEP31500101DTO.java v 0.1 2021-04-14
 * @description :
 */
@JacksonXmlRootElement(localName = "CloOrdrReq", namespace = "http://www.dcep.com/dcep/31500101/")
@Gateway(msgTp = "dcep.315.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class DCEP31500101DTO extends GwDTO {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1243889097349522796L;


	@Valid
    @NotNull
    @JacksonXmlProperty(localName = "GrpHdr")
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    private GrpHdr grpHdr;


    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    private OrgnlGrpHdr orgnlGrpHdr;

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

    public GrpHdr getGrpHdr() {
        return grpHdr;
    }

    public void setGrpHdr(GrpHdr grpHdr) {
        this.grpHdr = grpHdr;
    }

    public OrgnlGrpHdr getOrgnlGrpHdr() {
        return orgnlGrpHdr;
    }

    public void setOrgnlGrpHdr(OrgnlGrpHdr orgnlGrpHdr) {
        this.orgnlGrpHdr = orgnlGrpHdr;
    }

    @Override
    public String toString() {
        return "DCEP31500101DTO{" +
                "grpHdr=" + grpHdr +
                ", orgnlGrpHdr=" + orgnlGrpHdr +
                '}';
    }
}
