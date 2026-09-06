package com.dcep.supergw.dto.dc048;

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
 * 合约变更记录
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:39:01
 */
@Data
public class CtrctChngRcrd implements Serializable {
    /**
     * 合约变更记录Id
     */
    @JacksonXmlProperty(
            localName = "CtrctChngRcrdId"
    )
    @Length(
            min = 1,
            max = 64
    )
    @NotBlank
    private String ctrctChngRcrdId;

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
            regexp = "R377||R392||R393"
    )
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
     * 发起方合约实例
     */
    @JacksonXmlProperty(
            localName = "SndrCtrctInst"
    )
    @NotNull
    @Valid
    private SndrCtrctInst sndrCtrctInst;

    /**
     * 目标合约产品版本号
     */
    @JacksonXmlProperty(
            localName = "TrgtCtrctPdctVrsn"
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
    private String trgtCtrctPdctVrsn;

    /**
     * 原合约产品版本号
     */
    @JacksonXmlProperty(
            localName = "OrgnlCtrctPdctVrsn"
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
    private String orgnlCtrctPdctVrsn;

    /**
     * 协议
     */
    @JacksonXmlProperty(
            localName = "Agrmt"
    )
    @Valid
    private Agrmt agrmt;

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
