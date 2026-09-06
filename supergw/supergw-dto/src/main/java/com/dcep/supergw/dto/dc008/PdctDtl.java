package com.dcep.supergw.dto.dc008;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 组件ProductDetail
 *
 * @Author qinchaoyong
 * @date 2024-09-10 11:45:04
 */
@Data
public class PdctDtl implements Serializable {
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

    /**
     * 合约产品状态
     */
    @JacksonXmlProperty(
            localName = "PdctSts"
    )
    @Pattern(regexp = "SCPS01||SCPS02||SCPS03||SCPS04")
    private String pdctSts;

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

    @JacksonXmlProperty(
        localName = "MsgAuth"
    )
    @Length(
        min = 1,
        max = 5
    )
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    @NotBlank
    private String msgAuth;

    /**
     * 合约产品标签列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PdctLblList")
    @Valid
    private List<PdctLbl> PdctLblList;

    /**
     * 签约方列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "SgntrPtyList")
    @Valid
    @Size(min = 2, max = 99)
    private List<SgntrPty> sgntrPtyList;

    /**
     * 附件列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "AttchmntList")
    @Valid
    private List<Attchmntchmnt> attchmntList;

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
     * 创建时间
     */
    @JacksonXmlProperty(
            localName = "CretTm"
    )
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @NotBlank
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss"
    )
    private String cretTm;

    /**
     * 修改时间
     */
    @JacksonXmlProperty(
            localName = "ModfyTm"
    )
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss"
    )
    private String modfyTm;

    /**
     * 注销时间
     */
    @JacksonXmlProperty(
            localName = "LgOutTm"
    )
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss"
    )
    private String lgOutTm;

    /**
     * 组件--ProductVersionParameterList
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PdctVrsnParamList")
    @NotNull
    @Valid
    private List<PdctVrsnParam> pdctVrsnParamList;
}
