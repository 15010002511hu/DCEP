package com.dcep.supergw.dto.dc317;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.ActiveCurrencyAndAmount;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : DCEP31700101DTO.java v 0.1 2021-04-15
 * @description : 收款码刷新请求
 */
@JacksonXmlRootElement(localName = "QRCodeReq", namespace = "http://www.dcep.com/dcep/31700101/")
@Gateway(msgTp = "dcep.317.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class DCEP31700101DTO extends GwDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3287538792951827011L;

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
        return "DCEP31700101DTO{" +
                "grpHdr=" + grpHdr +
                ", orgnlGrpHdr=" + orgnlGrpHdr +
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
        return CheckUtils.requestMsgChk(header,grpHdr);
    }


    public class OrgnlGrpHdr {
        /**
         * 原报文标识号
         */
        @NotBlank
        @Length(min = 1, max = 35)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "OrgnlMsgId")
        private String orgnlMsgId;


        /**
         * 原发起机构
         */
        @NotBlank
        @Length(min = 1, max = 14)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "OrgnlInstgPty")
        private String orgnlInstgPty;


        /**
         * 原报文编号
         */
        @NotBlank
        @Length(min = 1, max = 15)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "OrgnlMT")
        private String orgnlMT;

    	/**
    	 * 原商户号
    	 */
    	@JacksonXmlProperty(localName = "OrgnlMrchntNo")
    	@NotBlank
    	@Length(min = 1, max = 35)
    	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    	private String orgnlMrchntNo;
    	
        /**
         * 原商户订单号
         */
        @NotBlank
        @Length(min = 1, max = 64)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "OrgnlOutOrdrNo")
        private String orgnlOutOrdrNo;

        /**
         * 原订单号
         */
        @NotBlank
        @Length(min = 1, max = 64)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "OrgnlOrdrNo")
        private String orgnlOrdrNo;

        /**
         * 原订单金额
         */
        @Valid
        @NotNull
        @JacksonXmlProperty(localName = "OrgnlAmt")
        private ActiveCurrencyAndAmount orgnlAmt;

        /**
         * 原收款码
         */
        @NotBlank(groups = Priority.Highest.class)
        @Length(min = 1,max = 500)
        @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
        @JacksonXmlProperty(localName = "OrgnlQrCode")
        private String orgnlQrCode;

        public String getOrgnlMsgId() {
            return orgnlMsgId;
        }

        public void setOrgnlMsgId(String orgnlMsgId) {
            this.orgnlMsgId = orgnlMsgId;
        }

        public String getOrgnlInstgPty() {
            return orgnlInstgPty;
        }

        public void setOrgnlInstgPty(String orgnlInstgPty) {
            this.orgnlInstgPty = orgnlInstgPty;
        }

        public String getOrgnlMT() {
            return orgnlMT;
        }

        public void setOrgnlMT(String orgnlMT) {
            this.orgnlMT = orgnlMT;
        }

        public String getOrgnlMrchntNo() {
            return orgnlMrchntNo;
        }

        public void setOrgnlMrchntNo(String orgnlMrchntNo) {
            this.orgnlMrchntNo = orgnlMrchntNo;
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

        public String getOrgnlQrCode() {
            return orgnlQrCode;
        }

        public void setOrgnlQrCode(String orgnlQrCode) {
            this.orgnlQrCode = orgnlQrCode;
        }

        @Override
        public String toString() {
            return "OrgnlGrpHdr{" +
                    "orgnlMsgId='" + orgnlMsgId + '\'' +
                    ", orgnlInstgPty='" + orgnlInstgPty + '\'' +
                    ", orgnlMT='" + orgnlMT + '\'' +
                    ", orgnlMrchntNo='" + orgnlMrchntNo + '\'' +
                    ", orgnlOutOrdrNo='" + orgnlOutOrdrNo + '\'' +
                    ", orgnlOrdrNo='" + orgnlOrdrNo + '\'' +
                    ", orgnlAmt=" + orgnlAmt +
                    ", orgnlQrCode='" + orgnlQrCode + '\'' +
                    '}';
        }
    }
}
