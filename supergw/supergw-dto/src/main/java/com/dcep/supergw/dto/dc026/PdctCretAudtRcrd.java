package com.dcep.supergw.dto.dc026;

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
 * 合约产品创建审核记录
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:47:11
 */
@Data
public class PdctCretAudtRcrd implements Serializable {
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
     * 合约运营机构
     */
    @JacksonXmlProperty(
            localName = "OprInstnId"
    )
    @Length(
            min = 1,
            max = 14
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String oprInstnId;

    /**
     * 合约服务机构
     */
    @JacksonXmlProperty(
            localName = "SvcInstnId"
    )
    @Length(
            min = 1,
            max = 14
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String svcInstnId;

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
     * 审核状态
     */
    @JacksonXmlProperty(
            localName = "AudtSts"
    )
    @NotBlank
    @Pattern(regexp = "BAS01||BAS02||BAS03")
    private String audtSts;

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
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String tmpltId;

    /**
     * 审核时间
     */
    @JacksonXmlProperty(
            localName = "AudtTm"
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
    private String audtTm;

    /**
     * 合约产品标签列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PdctLblList")
    @Valid
    private List<PdctLbl> pdctLblList;
}
