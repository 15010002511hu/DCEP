package com.dcep.supergw.dto.dc318;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check318Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : DCEP31800101DTO.java v 0.1 2021-04-15
 * @description : 收款码刷新应答报文
 */
@JacksonXmlRootElement(localName = "QRCodeRsp",namespace = "http://www.dcep.com/dcep/31800101/")
@Gateway(msgTp = "dcep.318.001.01",isReturn = true)
public class DCEP31800101DTO extends GwDTO {

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

    @Valid
    @NotNull(groups = Priority.Highest.class)
    @Check318Biz
    @JacksonXmlProperty(localName = "RspsnInf")
    private RspsnInf rspsnInf;

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

    @Override
    public String toString() {
        return "DCEP31800101DTO{" +
                "grpHdr=" + grpHdr +
                ", orgnlGrpHdr=" + orgnlGrpHdr +
                ", rspsnInf=" + rspsnInf +
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
        return CheckUtils.responseMsgChk(header,grpHdr,orgnlGrpHdr);
    }


    public class RspsnInf{
        /**
         * 业务回执状态
         */
        @NotBlank(groups = Priority.Highest.class)
        @Length(min = 4,max = 4)
        @Pattern(regexp = "PR00||PR01")
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "RspsnSts")
        private String rspsnSts;

        /**
         * 业务拒绝码
         */
        @Length(min = 4,max = 4)
        @Pattern(regexp = "R[0-9]{3}")
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "RjctCd")
        private String rjctCd;

        /**
         * 业务拒绝信息
         */
        @Length(min = 1,max = 105)
        @JacksonXmlProperty(localName = "RjctInf")
        private String rjctInf;


        /**
         * 收款码
         */
        @Length(min = 1,max = 500)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "QrCode")
        private String qrCode;

        public String getRspsnSts() {
            return rspsnSts;
        }

        public void setRspsnSts(String rspsnSts) {
            this.rspsnSts = rspsnSts;
        }

        public String getRjctCd() {
            return rjctCd;
        }

        public void setRjctCd(String rjctCd) {
            this.rjctCd = rjctCd;
        }

        public String getRjctInf() {
            return rjctInf;
        }

        public void setRjctInf(String rjctInf) {
            this.rjctInf = rjctInf;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        @Override
        public String toString() {
            return "RspsnInf{" +
                    "rspsnSts='" + rspsnSts + '\'' +
                    ", rjctCd='" + rjctCd + '\'' +
                    ", rjctInf='" + rjctInf + '\'' +
                    ", qrCode='" + qrCode + '\'' +
                    '}';
        }
    }
}
