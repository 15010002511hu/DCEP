package com.dcep.supergw.dto.dc323;

import com.dcep.common.validator.Priority;
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
public class CdtrInf implements Serializable{
    private static final long serialVersionUID = -3192906051446091083L;

    /**
     * 收款运营机构
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 14)
    @JacksonXmlProperty(localName = "CdtrPtyId")
    private String cdtrPtyId;

    /**
     * 收款钱包名称
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 60)
    @JacksonXmlProperty(localName = "CdtrWltNm")
    private String cdtrWltNm;

    /**
     * 收款钱包ID
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 68)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "CdtrWltId")
    private String cdtrWltId;

    /**
     * 收款钱包类型
     */
    @JacksonXmlProperty(localName = "CdtrWltTp")
    @Pattern(regexp = "WT[0-9]{2}")
    private String cdtrWltTp;

    /**
     * 收款钱包等级
     */
    @JacksonXmlProperty(localName = "CdtrWltLvl")
    @Pattern(regexp = "WL[0-9]{2}")
    private String cdtrWltLvl;

}
