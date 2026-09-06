package com.dcep.supergw.dto.dc313;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : OrgnlGrpHdr.java v 0.1 2021-04-13
 * @description : 原交易请求头
 */
public class OrgnlGrpHdr implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9162309185836466767L;

	/**
     * 原报文标识号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "OrgnlMsgId")
    private String orgnlMsgId;

    /**
     * 原发起机构
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "OrgnlInstgPty")
    private String orgnlInstgPty;

    /**
     * 原报文编号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 15)
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
     * 原商户退款单号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "OrgnlOutRefNo")
    private String orgnlOutRefNo;

    /**
     * 原退款单号
     */
    @Length(min = 1,max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "OrgnlRefNo")
    private String orgnlRefNo;


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

    public String getOrgnlOutRefNo() {
        return orgnlOutRefNo;
    }

    public void setOrgnlOutRefNo(String orgnlOutRefNo) {
        this.orgnlOutRefNo = orgnlOutRefNo;
    }

    public String getOrgnlRefNo() {
        return orgnlRefNo;
    }

    public void setOrgnlRefNo(String orgnlRefNo) {
        this.orgnlRefNo = orgnlRefNo;
    }

    public String getOrgnlMrchntNo() {
		return orgnlMrchntNo;
	}

	public void setOrgnlMrchntNo(String orgnlMrchntNo) {
		this.orgnlMrchntNo = orgnlMrchntNo;
	}

	@Override
    public String toString() {
        return "OrgnlGrpHdr{" +
                "orgnlMsgId='" + orgnlMsgId + '\'' +
                ", orgnlInstgPty='" + orgnlInstgPty + '\'' +
                ", orgnlMT='" + orgnlMT + '\'' +
                ", orgnlOutRefNo='" + orgnlOutRefNo + '\'' +
                ", orgnlRefNo='" + orgnlRefNo + '\'' +
                '}';
    }
}
