package com.dcep.supergw.dto.dc306;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check306Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author weiqianjing
 * @Date 2021/9/26 18:11
 * @Description:收款运营机构通过此报文返回给受理机构子钱包支付结果
 */
@JacksonXmlRootElement(localName = "SubWltPmtRsp", namespace = "http://www.dcep.com/dcep/30600101/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.306.001.01", isReturn = true)
@Check306Biz
public class Dcep30600101DTO extends GwDTO {

    private static final long serialVersionUID = -1298928996845319813L;
    /**
     * 业务头组件GrpHdr
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lower.class)
    @CheckGrpHdrMsgId(groups = Priority.Lower.class)
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
     * 应答报文
     */
    @JacksonXmlProperty(localName = "RspsnInf")
    @NotNull
    @Valid
    private RspsnInf rspsnInf;

    @JacksonXmlProperty(localName = "PrmtInf")
    @Valid
    private PrmtInf prmtInf;

    /**
     * 收款人合约应答
     */
    @JacksonXmlProperty(
            localName = "CdtrCtrctRspsn"
    )
    @Valid
    private CdtrCtrctRspsn cdtrCtrctRspsn;

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        return CheckUtils.responseMsgChk(header, grpHdr, orgnlGrpHdr);
    }


    @Override
    public String fetchResultCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(rspsnInf.getRspsnSts());
        if (StringUtils.isNotEmpty(rspsnInf.getRjctCd())) {
            sb.append("-");
            sb.append(rspsnInf.getRjctCd());
        }
        return sb.toString();
    }
}
