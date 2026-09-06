package com.dcep.supergw.dto.dc721;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
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
 * 硬件钱包单笔脱机转出核对报文<dcep.721.001.01>
 * @author duzhong
 * @version $Id: Dcep72100101DTO.java, v 0.1 2020年4月20日 上午11:32:47  duz  Exp $
 *
 */
@JacksonXmlRootElement(localName = "ConvertNtfctn",namespace = "http://www.dcep.com/dcep/72100101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.721.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class Dcep72100101DTO extends GwDTO {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4676675208460328726L;

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
     * 【离线转出通知信息】
     */
    @JacksonXmlProperty(localName = "OfflineConvertNtfctnInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    OfflineConvertNtfctnInf offlineConvertNtfctnInf;
    
    

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        return true;
    }
}
