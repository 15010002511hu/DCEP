package com.dcep.supergw.dto.dc302;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.DcepCache;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check302Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * 3.6.2 统一下单应答报文<dcep.302.001.01>
 *
 * @author duzhong
 * @version $Id: Dcep30200101DTO.java, v 0.1 2021年04月13日 上午10:07:20 duzhong Exp $
 */
@JacksonXmlRootElement(localName = "CreOrdrRsp", namespace = "http://www.dcep.com/dcep/30200101/")
@ToString
@Check302Biz(groups = Priority.Lowest.class)
@Gateway(msgTp = "dcep.302.001.01", isReturn = true)
public class Dcep30200101DTO extends GwDTO implements DataEncryption {

    /**
     *
     */
    private static final long serialVersionUID = -1283364706006772513L;

    public Dcep30200101DTO() {

    }

    /**
     * Body报文体
     */
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

    @JacksonXmlProperty(localName = "RspsnInf")
    @NotNull
    @Valid
    private RspsnInf rspsnInf;

    @JacksonXmlProperty(localName = "QRCodeInf")
    @Valid
    private QRCodeInf qRCodeInf;

    @JacksonXmlProperty(localName = "InvokeInf")
    @Valid
    private InvokeInf invokeInf;

    @Override
    public void init() {
        if ("PR02".equals(this.getRspsnInf().getRspsnSts())) {
            if (StringUtils.isNotBlank(this.getRspsnInf().getCreOrdrTp())) {
                if ("COT02".equals(this.getRspsnInf().getCreOrdrTp())) {
                    this.invokeInf = new InvokeInf();
                    this.invokeInf.setInvokeUrl(DcepCache.getInstance().getAppCallBackUrl());
                    this.invokeInf.setEncInf(initEncrytInfomation());
                } else if ("COT03".equals(this.getRspsnInf().getCreOrdrTp())) {
                    this.invokeInf = new InvokeInf();
                    this.invokeInf.setEncInf(initEncrytInfomation());
                }
            } else {
                if ("TT03".equals(this.getRspsnInf().getTrxTp())) {
                    this.invokeInf = new InvokeInf();
                    this.invokeInf.setInvokeUrl(DcepCache.getInstance().getAppCallBackUrl());
                    this.invokeInf.setEncInf(initEncrytInfomation());
                }
                if ("TT04".equals(this.getRspsnInf().getTrxTp())) {
                    this.invokeInf = new InvokeInf();
                    this.invokeInf.setInvokeUrl(DcepCache.getInstance().getAppCallBackUrl());
                    this.invokeInf.setEncInf(initEncrytInfomation());
                }
                if ("TT13".equals(this.getRspsnInf().getTrxTp())) {
                    this.invokeInf = new InvokeInf();
                    this.invokeInf.setInvokeUrl(DcepCache.getInstance().getAppCallBackUrl());
                    this.invokeInf.setEncInf(initEncrytInfomation());
                }
            }
        }
    }

    private String initEncrytInfomation() {
        StringBuilder plaintext = new StringBuilder();
        plaintext.append("{");
        plaintext.append("\"mrchntNo\":\"");
        plaintext.append(this.getRspsnInf().getMrchntNo());
        plaintext.append("\",");
        plaintext.append("\"ordrNo\":\"");
        plaintext.append(this.getRspsnInf().getOrdrNo());
        plaintext.append("\",");
        //trnAmt是标准文档中给出的json key值 txnAmt
        plaintext.append("\"txnAmt\":\"");
        plaintext.append(this.getRspsnInf().getTrxAmt().getValue());
        //新增的推送子信息域
        if (null != this.getRspsnInf().getPushSubWltInf()) {
            plaintext.append("\",");
            plaintext.append("\"pushSubWltInf\":");
            plaintext.append("{");
            plaintext.append("\"subMrchntNo\":\"");
            plaintext.append(this.getRspsnInf().getPushSubWltInf().getSubMrchntNo());
            plaintext.append("\",");
            plaintext.append("\"mblPhNo\":\"");
            plaintext.append(this.getRspsnInf().getPushSubWltInf().getMblPhNo());
            plaintext.append("\",");
            plaintext.append("\"mrchntAccount\":\"");
            plaintext.append(this.getRspsnInf().getPushSubWltInf().getMrchntAcctId());
            plaintext.append("\",");
            plaintext.append("\"verifyCode\":\"");
            plaintext.append(this.getRspsnInf().getPushSubWltInf().getVeryCd());
            plaintext.append("\"}");
            plaintext.append("}");
        } else {
            plaintext.append("\"}");
        }
        return plaintext.toString();
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
    public ChannelEnums routeChannel(SoapHeader header) {

        return ChannelEnums.DIRECT_FORWARD;
    }

    @Override
    public String fetchResultCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(rspsnInf.getRspsnSts());
        if(StringUtils.isNotEmpty(rspsnInf.getRjctCd())){
            sb.append("-");
            sb.append(rspsnInf.getRjctCd());
        }
        return sb.toString();
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        List<String> plainTextList = new ArrayList<>();
        if (this.invokeInf != null) {
            //对明文加密
            if (invokeInf.getEncInf() != null) {
                plainTextList.add(this.getInvokeInf().getEncInf());
            }
        }
        List<String> cipherTextList = encryptionHelper.encrypt(plainTextList);
        if (this.invokeInf != null) {
            if (invokeInf.getEncInf() != null) {
                invokeInf.setEncInf(cipherTextList.get(0));
            }
        }

    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
//        if (this.invokeInf != null) {
//            this.invokeInf.decryptData(encryptionHelper);
//        }
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

    public RspsnInf getRspsnInf() {
        return rspsnInf;
    }

    public void setRspsnInf(RspsnInf rspsnInf) {
        this.rspsnInf = rspsnInf;
    }

    public QRCodeInf getqRCodeInf() {
        return qRCodeInf;
    }

    public void setqRCodeInf(QRCodeInf qRCodeInf) {
        this.qRCodeInf = qRCodeInf;
    }

    public InvokeInf getInvokeInf() {
        return invokeInf;
    }

    public void setInvokeInf(InvokeInf invokeInf) {
        this.invokeInf = invokeInf;
    }

}
