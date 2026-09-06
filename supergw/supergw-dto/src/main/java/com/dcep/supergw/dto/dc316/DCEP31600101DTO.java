package com.dcep.supergw.dto.dc316;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check316Biz;
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
 * @version : DCEP31600101DTO.java v 0.1 2021-04-14
 * @description : 关闭订单应答报文
 */
@JacksonXmlRootElement(localName = "CloOrdrRsp",namespace = "http://www.dcep.com/dcep/31600101/")
@Gateway(msgTp = "dcep.316.001.01",isReturn = true)
public class DCEP31600101DTO extends GwDTO {

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
    @NotNull
    @Check316Biz
    @JacksonXmlProperty(localName = "RspsnInf")
    private RspsnInf rspsnInf;

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
        return "DCEP31600101DTO{" +
                "grpHdr=" + grpHdr +
                ", orgnlGrpHdr=" + orgnlGrpHdr +
                ", rspsnInf=" + rspsnInf +
                '}';
    }

    public class RspsnInf{

        /**
         *
         * 业务回执状态
         */
        @NotBlank(groups = Priority.Highest.class)
        @Length(min = 4,max = 4)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @Pattern(regexp = "PR00||PR01||PR02||PR03||PR04")
        @JacksonXmlProperty(localName = "RspsnSts")
        private String rspsnSts;

        /**
         *
         * 业务拒绝码
         */
        @Length(min = 4,max = 4)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @Pattern(regexp = "R[0-9]{3}")
        @JacksonXmlProperty(localName = "RjctCd")
        private String rjctCd;


        /**
         *
         * 业务拒绝信息
         */
        @Length(min = 1,max = 105)
        @JacksonXmlProperty(localName = "RjctInf")
        private String rjctInf;


        /**
         *
         * 原商户订单号
         */
        @NotBlank(groups = Priority.Highest.class)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @Length(min = 1,max = 64)
        @JacksonXmlProperty(localName = "OrgnlOutOrdrNo")
        private String orgnlOutOrdrNo;

        /**
         *
         * 原订单号
         */
        @Length(min = 1,max = 64)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "OrgnlOrdrNo")
        private String orgnlOrdrNo;

        /**
         *
         * 原订单金额
         */
        @Valid
        @NotNull
        @JacksonXmlProperty(localName = "OrgnlAmt")
        private ActiveCurrencyAndAmount orgnlAmt;

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

        public String getOrgnlOutOrdrNo() {
            return orgnlOutOrdrNo;
        }

        public void setOrgnlOutOrdrNo(String orgnlOutOrdrNo) {
            this.orgnlOutOrdrNo = orgnlOutOrdrNo;
        }

        public String getOrgnlOrdrNo() {
            return orgnlOrdrNo;
        }

        public void setOrgnlOrdrNo(String orgnlOrdrNo) {
            this.orgnlOrdrNo = orgnlOrdrNo;
        }

        public ActiveCurrencyAndAmount getOrgnlAmt() {
            return orgnlAmt;
        }

        public void setOrgnlAmt(ActiveCurrencyAndAmount orgnlAmt) {
            this.orgnlAmt = orgnlAmt;
        }



        @Override
        public String toString() {
            return "RspsnInf{" +
                    "rspsnSts='" + rspsnSts + '\'' +
                    ", rjctCd='" + rjctCd + '\'' +
                    ", rjctInf='" + rjctInf + '\'' +
                    ", orgnlOutOrdrNo='" + orgnlOutOrdrNo + '\'' +
                    ", orgnlOrdrNo='" + orgnlOrdrNo + '\'' +
                    ", orgnlAmt=" + orgnlAmt +
                    '}';
        }
    }

}
