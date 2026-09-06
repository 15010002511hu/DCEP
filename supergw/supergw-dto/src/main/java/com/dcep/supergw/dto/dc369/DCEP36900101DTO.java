package com.dcep.supergw.dto.dc369;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check369Biz;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : DCEP36900101DTO.java v 0.1 2021-04-14
 * @description : 支付结果通知报文
 */
@Setter
@Getter
@ToString
@JacksonXmlRootElement(localName = "PmtRsNtfctn", namespace = "http://www.dcep.com/dcep/36900101/")
@Gateway(msgTp = "dcep.369.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class DCEP36900101DTO extends GwDTO {

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
    @JacksonXmlProperty(localName = "RspsnInf")
    @Check369Biz
    private RspsnInf rspsnInf;

    @Valid
    @JacksonXmlProperty(localName = "PrmtInf")
    private PrmtInf prmtInf;

    /**
     * 收款人合约应答
     */
    @JacksonXmlProperty(
            localName = "CdtrCtrctRspsn"
    )
    @Valid
    private CdtrCtrctRspsn cdtrCtrctRspsn;

    /**
     * 付款人合约实例信息
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "DbtrCtrctInst")
    @Valid
    private List<DbtrCtrctInst> dbtrCtrctInst;

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

    public class RspsnInf {
        /**
         * 业务回执状态
         */
        @NotBlank(groups = Priority.Highest.class)
        @Length(min = 4, max = 4)
        @Pattern(regexp = "PR00||PR01||PR02||PR03||PR04")
        @JacksonXmlProperty(localName = "RspsnSts")
        private String rspsnSts;

        /**
         * 业务拒绝码
         */
        @Length(min = 4, max = 4)
        @Pattern(regexp = "R[0-9]{3}")
        @JacksonXmlProperty(localName = "RjctCd")
        private String rjctCd;


        /**
         * 业务拒绝信息
         */
        @Length(min = 1, max = 105)
        @JacksonXmlProperty(localName = "RjctInf")
        private String rjctInf;

        /**
         * 订单金额
         */
        @Valid
        @NotNull(groups = Priority.Highest.class)
        @JacksonXmlProperty(localName = "TrxAmt")
        private ActiveCurrencyAndAmount trxAmt;

        /**
         * 商户订单号
         */
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @Length(min = 1, max = 64)
        @JacksonXmlProperty(localName = "OutOrdrNo")
        private String outOrdrNo;

        /**
         * 订单号
         */
        @NotBlank(groups = Priority.Highest.class)
        @Length(min = 1, max = 64)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "OrdrNo")
        private String ordrNo;

        /**
         * 交易完成时间
         */
        @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}",
                message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
        @JacksonXmlProperty(localName = "TrxFinishTm")
        private String trxFinishTm;


        /**
         * 交易批次号
         */
        @Length(min = 1, max = 13)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "BatchId")
        private String batchId;

        /**
         * 交易类型
         */
        @JacksonXmlProperty(localName = "TrxTp")
        @Pattern(regexp = "TT[0-9]{2}")
        private String trxTp;

        /**
         * 收/付标识
         */
        @JacksonXmlProperty(localName = "RPFlg")
        @Pattern(regexp = "RPF01||RPF02||RP01||RP02")
        private String rPFlg;

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

        public ActiveCurrencyAndAmount getTrxAmt() {
            return trxAmt;
        }

        public void setTrxAmt(ActiveCurrencyAndAmount trxAmt) {
            this.trxAmt = trxAmt;
        }

        public String getOutOrdrNo() {
            return outOrdrNo;
        }

        public void setOutOrdrNo(String outOrdrNo) {
            this.outOrdrNo = outOrdrNo;
        }

        public String getOrdrNo() {
            return ordrNo;
        }

        public void setOrdrNo(String ordrNo) {
            this.ordrNo = ordrNo;
        }

        public String getTrxFinishTm() {
            return trxFinishTm;
        }

        public void setTrxFinishTm(String trxFinishTm) {
            this.trxFinishTm = trxFinishTm;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getTrxTp() {
            return trxTp;
        }

        public void setTrxTp(String trxTp) {
            this.trxTp = trxTp;
        }

        @JsonIgnore
        public String getRPFlg() {
            return rPFlg;
        }

        @JsonIgnore
        public void setRPFlg(String rPFlg) {
            this.rPFlg = rPFlg;
        }

        @Override
        public String toString() {
            return "RspsnInf{" +
                    "rspsnSts='" + rspsnSts + '\'' +
                    ", rjctCd='" + rjctCd + '\'' +
                    ", rjctInf='" + rjctInf + '\'' +
                    ", trxAmt=" + trxAmt +
                    ", outOrdrNo='" + outOrdrNo + '\'' +
                    ", ordrNo='" + ordrNo + '\'' +
                    ", trxFinishTm='" + trxFinishTm + '\'' +
                    ", batchId='" + batchId + '\'' +
                    ", trxTp='" + trxTp + '\'' +
                    ", rPFlg='" + rPFlg + '\'' +
                    '}';
        }
    }
}
