package com.dcep.supergw.dto.dc028;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 产品创建审核详情信息
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:52:00
 */
@Data
public class PdctCretAudtDtlInf implements Serializable {
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
    @NotBlank
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
     * 合约模板ID
     */
    @JacksonXmlProperty(
            localName = "TmpltId"
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
    private String tmpltId;

    @JacksonXmlProperty(
        localName = "MsgAuth"
    )
    @Length(
        min = 1,
        max = 5
    )
    @NotBlank
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    String msgAuth;

    @JacksonXmlProperty(
        localName = "AutoRel"
    )
    @Length(
        min = 1,
        max = 5
    )
    @NotBlank
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    String autoRel;

    /**
     * 合约产品标签列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PdctLblList")
    @Size(max = 5)
    @Valid
    private List<PdctLbl> pdctLblList;

    /**
     * 签约方列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "SgntrPtyList")
    @Size(min = 2, max = 99)
    @Valid
    private List<SgntrPty> sgntrPtyList;

    /**
     * 附件列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "AttchmntchmntList")
    @Valid
    private List<Attchmnt> attchmntchmntList;

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
     * 首个产品版本参数
     */
    @JacksonXmlProperty(
            localName = "InitlPdctVrsnParams"
    )
    @NotNull
    @Valid
    private InitlPdctVrsnParams initlPdctVrsnParams;

    /**
     * 产品版本参数
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PdctVrsnParams")
    @Valid
    private List<PdctVrsnParams> pdctVrsnParams;

    /**
     * 审核信息
     */
    @JacksonXmlProperty(
            localName = "AudtDtlInf"
    )
    @NotNull
    @Valid
    private AudtDtlInf audtDtlInf;
}
