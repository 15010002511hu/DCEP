package com.dcep.supergw.dto.dc419;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 8.3.6 商户及订单查询请求报文<dcep.419.001.01>
 * @author duzhong
 */
@JacksonXmlRootElement(localName = "BndngAcctQryReq",namespace = "http://www.dcep.com/dcep/41900101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.419.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class Dcep41900101DTO extends GwDTO {

    private static final long serialVersionUID = -7231835772911811423L;


    /**
     * 【业务头组件】
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @Valid
    private GrpHdr grpHdr;

    
    /**
     * 原报文主键组件
     */
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    @NotNull
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;

    
    
    /**
     * 【原签约信息】
     */
    @JacksonXmlProperty(localName = "OrgnlSgnInf")
    @NotNull
    @Valid
    private OrgnlSgnInf orgnlSgnInf;

   

    @Override
    public void init(){

    }

    @Override
    public String fetchMsgId(){
        return grpHdr.getMsgId();
    }

    @Override

	public boolean check(SoapHeader header) {
        return CheckUtils.requestMsgChk(header, grpHdr);
    }

}