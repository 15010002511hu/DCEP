package com.dcep.supergw.dto.dc054;

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
 * 合约解约记录
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:49:27
 */
@Data
public class CtrctCclRcrd implements Serializable {
    /**
     * 合约解约记录Id
     */
    @JacksonXmlProperty(
            localName = "CtrctCclRcrdId"
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
    private String ctrctCclRcrdId;

    /**
     * 记录状态
     */
    @JacksonXmlProperty(
            localName = "RcrdSts"
    )
    @NotBlank
    @Pattern(
            regexp = "SCRS01||SCRS02||SCRS03"
    )
    private String rcrdSts;

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
     * 智能合约服务机构
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
     * 合约实例ID
     */
    @JacksonXmlProperty(
            localName = "CtrctInstId"
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
    private String ctrctInstId;

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
     * 合约产品版本号
     */
    @JacksonXmlProperty(
            localName = "CtrctPdctVrsn"
    )
    @Length(
            min = 1,
            max = 11
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ctrctPdctVrsn;

    /**
     * 截止时间
     */
    @JacksonXmlProperty(
            localName = "Ddln"
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
    private String ddln;

    /**
     * 签约方列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "CtrctPtyList")
    @NotNull
    @Size(min = 2, max = 99)
    @Valid
    private List<CtrctPty> ctrctPtyList;
}
