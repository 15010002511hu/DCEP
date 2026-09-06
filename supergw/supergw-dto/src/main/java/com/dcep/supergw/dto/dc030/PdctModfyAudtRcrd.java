package com.dcep.supergw.dto.dc030;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 合约产品创建审核记录
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:48:38
 */
@Data
public class PdctModfyAudtRcrd implements Serializable {
    /**
     * 合约产品ID
     */
    @JacksonXmlProperty(
            localName = "PdctId"
    )
    @Length(
            min = 1,
            max = 16
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String pdctId;

    /**
     * 申请流水号
     */
    @JacksonXmlProperty(
            localName = "SrlNb"
    )
    @Length(
            min = 1,
            max = 64
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String srlNb;

    /**
     * 合约产品名称
     */
    @JacksonXmlProperty(
            localName = "PdctNm"
    )
    @Length(
            min = 1,
            max = 40
    )
    private String pdctNm;

    /**
     * 合约产品描述
     */
    @JacksonXmlProperty(
            localName = "PdctDesc"
    )
    @Length(
            min = 1,
            max = 400
    )
    @NotBlank
    private String pdctDesc;

    /**
     * 合约产品标签列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PdctLblList")
    @Valid
    private List<PdctLbl> pdctLblList;

    /**
     * 联系业务部门
     */
    @JacksonXmlProperty(
            localName = "BizDeptCtct"
    )
    @Length(
            min = 1,
            max = 60
    )
    @NotBlank
    private String bizDeptCtct;

    /**
     * 联系人
     */
    @JacksonXmlProperty(
            localName = "Ctct"
    )
    @Length(
            min = 1,
            max = 28
    )
    @NotBlank
    private String ctct;

    /**
     * 联系方式
     */
    @JacksonXmlProperty(
            localName = "CtctInf"
    )
    @Length(
            min = 1,
            max = 70
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ctctInf;

    /**
     * 附言
     */
    @JacksonXmlProperty(
            localName = "Postscript"
    )
    @Length(
            min = 1,
            max = 120
    )
    @NotBlank
    private String postscript;

    /**
     * 审核信息
     */
    @JacksonXmlProperty(
            localName = "AudtInf"
    )
    @NotNull
    @Valid
    private AudtInf audtInf;
}
