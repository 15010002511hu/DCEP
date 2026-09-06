package com.dcep.supergw.dto.dc314;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check314Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : DCEP31400101DTO.java v 0.1 2021-04-14
 * @description : 退款结果查询应答报文
 */
@JacksonXmlRootElement(localName = "RefRsQryRsp", namespace = "http://www.dcep.com/dcep/31400101/")
@Gateway(msgTp = "dcep.314.001.01", isReturn = true)
@Check314Biz(groups = Priority.Lowest.class)
public class DCEP31400101DTO extends GwDTO {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1822766119662318632L;

	@Valid
    @NotNull
    @JacksonXmlProperty(localName = "GrpHdr")
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    private GrpHdr grpHdr;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "BizQryRef")
    private BizQryRef bizQryRef;

    @Valid
    @JacksonXmlProperty(localName = "BizRpt")
    private BizRpt bizRpt;

    @Valid
    @JacksonXmlProperty(localName = "OprlErr")
    private OprlErr oprlErr;


    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        //原请求报文标识号
        String headerMsgId = header.getMsgSN().substring(0, 32);
        //原请求报文标识号
        String requestMsgId = bizQryRef.getQryRef();
        if(!headerMsgId.equals(requestMsgId)){
            throw new DcepException(ErrorEnum.MSGSN_MSGID_NOT_MATCH_ERROR);
        }

        //报文头报文发送方
        String headerSender = header.getSender();
        //报文体报文发送方
        String bodySender = grpHdr.getInstgPty().getInstgDrctPty();
        if(!headerSender.equals(bodySender)){
            throw new DcepException(ErrorEnum.SENDER_NOT_MATCH_ERROR);
        }

        //报文头报文接收方
        String headerReceiver = header.getReceiver();
        //报文体报文接收方
        String bodyReceiver = grpHdr.getInstdPty().getInstdDrctPty();
        //原报文发送方
        String rawSender = bizQryRef.getQryNm();
        if(!headerReceiver.equals(bodyReceiver) || !headerReceiver.equals(rawSender)){
            throw new DcepException(ErrorEnum.RECEIVER_NOT_MATCH_ERROR);
        }

        return true;
    }

    public GrpHdr getGrpHdr() {
        return grpHdr;
    }

    public void setGrpHdr(GrpHdr grpHdr) {
        this.grpHdr = grpHdr;
    }

    public BizQryRef getBizQryRef() {
        return bizQryRef;
    }

    public void setBizQryRef(BizQryRef bizQryRef) {
        this.bizQryRef = bizQryRef;
    }

    public OprlErr getOprlErr() {
        return oprlErr;
    }

    public void setOprlErr(OprlErr oprlErr) {
        this.oprlErr = oprlErr;
    }

    public BizRpt getBizRpt() {
        return bizRpt;
    }

    public void setBizRpt(BizRpt bizRpt) {
        this.bizRpt = bizRpt;
    }

    @Override
    public String toString() {
        return "DCEP31400101DTO{" +
                "grpHdr=" + grpHdr +
                ", BizQryRef=" + bizQryRef +
                ", bizRpt=" + bizRpt +
                ", oprlErr=" + oprlErr +
                '}';
    }

    @Override
    public String fetchResultCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(bizQryRef.getQryRs());
        if (oprlErr != null) {
            if (StringUtils.isNotEmpty(oprlErr.getErr().getRjctCd())) {
                sb.append("-");
                sb.append(oprlErr.getErr().getRjctCd());
            }
        }
        return sb.toString();
    }
}
