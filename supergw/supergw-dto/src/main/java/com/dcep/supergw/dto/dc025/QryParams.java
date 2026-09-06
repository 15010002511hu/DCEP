package com.dcep.supergw.dto.dc025;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 查询参数
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:47:37
 */
@Data
public class QryParams implements Serializable {
    /**
     * 请求分页信息
     */
    @JacksonXmlProperty(
            localName = "ReqPgInf"
    )
    @NotNull
    @Valid
    private ReqPgInf reqPgInf;

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
     * 合约产品标签
     */
    @JacksonXmlProperty(
            localName = "PdctLabl"
    )
    @Length(
            min = 1,
            max = 40
    )
    private String pdctLabl;

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
    @Pattern(regexp = "BAS01||BAS02||BAS03")
    private String audtSts;
}
