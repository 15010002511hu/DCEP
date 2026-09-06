package com.dcep.supergw.dto.dc035;

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
 * 组件QueryParameter
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:55:21
 */
@Data
public class QryParam implements Serializable {
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
     * 签约方运营机构
     */
    @JacksonXmlProperty(
            localName = "SgntrPtyId"
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
    private String sgntrPtyId;

    /**
     * 签约方列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "SgntrPtyList")
    @NotNull
    @Size(min = 1, max = 99)
    @Valid
    private List<SgntrPty> sgntrPtyList;
}
