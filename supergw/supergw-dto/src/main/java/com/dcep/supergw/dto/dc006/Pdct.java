package com.dcep.supergw.dto.dc006;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 合约产品列表
 *
 * @Author qinchaoyong
 * @date 2024-09-10 10:56:34
 */
@Data
public class Pdct implements Serializable {
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
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String pdctId;

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

    /**
     * 合约产品标签列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PdctLblList")
    @Valid
    private List<PdctLbl> pdctLblList;

    /**
     * 合约产品状态
     */
    @JacksonXmlProperty(
            localName = "PdctSts"
    )
    @NotBlank
    @Pattern(regexp = "SCPS01||SCPS02||SCPS03||SCPS04")
    private String pdctSts;

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
}
