package com.dcep.supergw.dto.dc315;

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
 * @version : OrgnlGrpHdr.java v 0.1 2021-04-14
 * @description :
 */
public class OrgnlGrpHdr implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 589218888125718889L;

	/**
     * 原报文标识号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "OrgnlMsgId")
    private String orgnlMsgId;

    /**
     * 原发起机构
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "OrgnlInstgPty")
    private String orgnlInstgPty;

    /**
     * 原报文编号
     */
    @NotBlank(groups = Priority.Highest.class)
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
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "OrgnlOutOrdrNo")
    private String orgnlOutOrdrNo;

    /**
     * 原订单号
     */
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

    @Override
    public String toString() {
        return "OrgnlGrpHdr{" +
                "orgnlMsgId='" + orgnlMsgId + '\'' +
                ", orgnlInstgPty='" + orgnlInstgPty + '\'' +
                ", orgnlMT='" + orgnlMT + '\'' +
                ", orgnlOutOrdrNo='" + orgnlOutOrdrNo + '\'' +
                ", orgnlOrdrNo='" + orgnlOrdrNo + '\'' +
                ", orgnlAmt=" + orgnlAmt +
                '}';
    }
}
