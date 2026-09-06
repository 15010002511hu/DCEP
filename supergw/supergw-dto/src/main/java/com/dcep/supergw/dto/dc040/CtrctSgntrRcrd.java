package com.dcep.supergw.dto.dc040;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 合约签约记录
 *
 * @Author qinchaoyong
 * @date 2024-09-11 14:12:11
 */
@Data
public class CtrctSgntrRcrd implements Serializable {
    /**
     * 合约签约记录ID
     */
    @JacksonXmlProperty(
            localName = "CtrctSgntrRcrdId"
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
    private String ctrctSgntrRcrdId;

    /**
     * 合约签约记录状态
     */
    @JacksonXmlProperty(
            localName = "CtrctSgntrRcrdSts"
    )
    @NotBlank
    @Pattern(
            regexp = "SCRS01||SCRS02||SCRS03"
    )
    private String ctrctSgntrRcrdSts;

    /**
     * 失败原因
     */
    @JacksonXmlProperty(
            localName = "FailrRsn"
    )
    @Length(
            min = 1,
            max = 5
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    @Pattern(regexp = "R[0-9]{3}")
    private String failrRsn;

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
     * 产品版本号
     */
    @JacksonXmlProperty(
            localName = "PdctVrsn"
    )
    @Length(
            min = 1,
            max = 11
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String pdctVrsn;

    /**
     * 合约参数
     */
    @JacksonXmlProperty(
            localName = "CtrctParam"
    )
    @NotNull
    @Valid
    private CtrctParam ctrctParam;

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
    @JacksonXmlProperty(localName = "SgntrPtyList")
    @Size(min = 2, max = 99)
    @Valid
    private List<SgntrPty> sgntrPtyList;

}
