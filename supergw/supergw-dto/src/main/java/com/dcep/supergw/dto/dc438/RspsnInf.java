package com.dcep.supergw.dto.dc438;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Getter
@Setter
@ToString
public class RspsnInf implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7337136458266304891L;
	
    /**
     * 业务回执状态 
     */
    @JacksonXmlProperty(localName = "RspsnSts")
    @NotBlank
    @Length(min = 4, max = 4)
    @Pattern(regexp = "PR00||PR01||PR02||PR03||PR04")
    private String rspsnSts;

    /**
     * 业务拒绝码
     */
    @JacksonXmlProperty(localName = "RjctCd")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "R[0-9]{3}")
    private String rjctCd;

    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(localName = "RjctInf")
    @Length(min = 1, max = 105)
    private String rjctInf;

    /**
     * 管理类型
     */
    @JacksonXmlProperty(localName = "MgmtTp")
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "MT01||MT02||MT03||MT04||MT05||MT06||MT07||MT08")
    private String mgmtTp;

    /**
     * 签约协议号
     */
    @JacksonXmlProperty(localName = "PtcId")
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 60)
    private String ptcId;

    /**
     * 业务上下文
     */
    @JacksonXmlProperty(localName = "ContextNo")
    @Length(min = 1,max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String contextNo;

}
