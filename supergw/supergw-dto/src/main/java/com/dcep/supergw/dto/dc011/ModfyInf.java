package com.dcep.supergw.dto.dc011;

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
 * 合约产品信息修改请求参数
 *
 * @Author qinchaoyong
 * @date 2024-09-10 11:53:40
 */
@Data
public class ModfyInf implements Serializable {
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
     * 合约产品标签列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PdctLblList")
    @Valid
    private List<PdctLbl> pdctLblList;

    /**
     * 签约方列表
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "SgntrPtyList")
    @NotNull
    @Size(min = 2, max = 99)
    @Valid
    private List<SgntrPty> sgntrPtyList;

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
    private String postscript;
}
