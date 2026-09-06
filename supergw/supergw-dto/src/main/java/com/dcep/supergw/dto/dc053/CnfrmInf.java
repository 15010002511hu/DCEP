package com.dcep.supergw.dto.dc053;

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
 * 合约变更确认信息
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:46:57
 */
@Data
public class CnfrmInf implements Serializable {
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
     * 签约方运营机构
     */
    @JacksonXmlProperty(
            localName = "CtrctPtyId"
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
    private String ctrctPtyId;

    /**
     * 合约参数
     */
    @JacksonXmlProperty(
            localName = "CtrctParam"
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ctrctParam;

    /**
     * 签约方列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "CtrctPtyList")
    @Size(min = 1, max = 99)
    @NotNull
    @Valid
    private List<CtrctPty> ctrctPtyList;
}
