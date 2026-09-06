package com.dcep.supergw.dto.dc314;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : OrgnlTxInf.java v 0.1 2021-04-14
 * @description :
 */
public class OrgnlTxInf implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7039274665087780103L;

	/**
     * 原报文标识号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 35)
    @JacksonXmlProperty(localName = "OrgnlMsgId")
    private String orgnlMsgId;

    /**
     * 原发起机构
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 14)
    @JacksonXmlProperty(localName = "OrgnlInstgPty")
    private String orgnlInstgPty;

    /**
     * 原报文编号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 16)
    @JacksonXmlProperty(localName = "OrgnlMT")
    private String orgnlMT;

    /**
     * 原退款金额
     */
    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "OrgnlTrxAmt")
    private ActiveCurrencyAndAmount orgnlTrxAmt;

    /**
     * 退款单号生成时间
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    @JacksonXmlProperty(localName = "OrgnlRefTm")
    private String orgnlRefTm;
    
    /**
     * 原商户订单号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 64)
    @JacksonXmlProperty(localName = "OrgnlOutRefNo")
    private String orgnlOutRefNo;
    

    /**
     * 原商户退款单号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 64)
    @JacksonXmlProperty(localName = "OrgnlOrdrNo")
    private String orgnlOrdrNo;

    /**
     * 原交易完成时间
     */
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    @JacksonXmlProperty(localName = "OrgnlTrxFinishTm")
    private String orgnlTrxFinishTm;

    /**
     * 原交易批次号
     */
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1,max = 13)
    @JacksonXmlProperty(localName = "OrgnlBatchId")
    private String orgnlBatchId;

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

    public ActiveCurrencyAndAmount getOrgnlTrxAmt() {
        return orgnlTrxAmt;
    }

    public void setOrgnlTrxAmt(ActiveCurrencyAndAmount orgnlTrxAmt) {
        this.orgnlTrxAmt = orgnlTrxAmt;
    }

    public String getOrgnlRefTm() {
        return orgnlRefTm;
    }

    public void setOrgnlRefTm(String orgnlRefTm) {
        this.orgnlRefTm = orgnlRefTm;
    }

    public String getOrgnlOutRefNo() {
        return orgnlOutRefNo;
    }

    public void setOrgnlOutRefNo(String orgnlOutRefNo) {
        this.orgnlOutRefNo = orgnlOutRefNo;
    }

    public String getOrgnlOrdrNo() {
        return orgnlOrdrNo;
    }

    public void setOrgnlOrdrNo(String orgnlOrdrNo) {
        this.orgnlOrdrNo = orgnlOrdrNo;
    }

    public String getOrgnlTrxFinishTm() {
        return orgnlTrxFinishTm;
    }

    public void setOrgnlTrxFinishTm(String orgnlTrxFinishTm) {
        this.orgnlTrxFinishTm = orgnlTrxFinishTm;
    }

    public String getOrgnlBatchId() {
        return orgnlBatchId;
    }

    public void setOrgnlBatchId(String orgnlBatchId) {
        this.orgnlBatchId = orgnlBatchId;
    }

    @Override
    public String toString() {
        return "OrgnlTxInf{" +
                "orgnlMsgId='" + orgnlMsgId + '\'' +
                ", orgnlInstgPty='" + orgnlInstgPty + '\'' +
                ", orgnlMT='" + orgnlMT + '\'' +
                ", orgnlTrxAmt=" + orgnlTrxAmt +
                ", orgnlRefTm='" + orgnlRefTm + '\'' +
                ", orgnlOrdrNo='" + orgnlOrdrNo + '\'' +
                ", orgnlTrxFinishTm='" + orgnlTrxFinishTm + '\'' +
                ", orgnlBatchId='" + orgnlBatchId + '\'' +
                '}';
    }
}
