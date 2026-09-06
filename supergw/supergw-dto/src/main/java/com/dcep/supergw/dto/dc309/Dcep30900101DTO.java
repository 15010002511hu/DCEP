package com.dcep.supergw.dto.dc309;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.CheckBizCode;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 3.6.1 订单退款请求报文<dcep.309.001.01>
 *
 * @author duzhong
 * @version $Id: Dcep30900101DTO.java, v 0.1 2021年04月14日 上午10:07:20 duzhong Exp
 * $
 */
@JacksonXmlRootElement(localName = "OrdrRefReq", namespace = "http://www.dcep.com/dcep/30900101/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.309.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@CheckBizCode(bizCtgyCode = "refInf.trxCtgyPurpCd", bizTypeCode = "refInf.trxBizTp")
public class Dcep30900101DTO extends GwDTO {

    /**
     *
     */
    private static final long serialVersionUID = -1283364706006772513L;

    public Dcep30900101DTO() {

    }

    /**
     * Body报文体
     */

    /**
     * 业务头组件GrpHdr
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
     * 订单退款信息
     */
    @JacksonXmlProperty(localName = "RefInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private RefInf refInf;
    /**
     * 原订单信息
     */
    @JacksonXmlProperty(localName = "OrgnlOrdrInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private OrgnlOrdrInf orgnlOrdrInf;
    /**
     * 二级商户退款列表
     */
    @JacksonXmlElementWrapper(localName = "SubMrchntRefList")
    @JacksonXmlProperty(localName = "SubMrchntRefInf")
    @Valid
    private List<SubMrchntRefInf> subMrchntRefList;

    @JacksonXmlProperty(localName = "CpnInf")
    @Valid
    private CpnInf cpnInf;

    @JacksonXmlProperty(localName = "CtrctParam")
    @Valid
    CtrctParam ctrctParam;

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        return true;
    }

    @Override
    public ChannelEnums routeChannel(SoapHeader header) {

        return ChannelEnums.DIRECT_FORWARD;
    }


    @Override
    public String fetchResultCode() {
        return "PR00-";
    }

    @Override
    public void init() {
    }
}
