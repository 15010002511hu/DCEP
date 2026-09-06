package com.dcep.supergw.dto.dc005;

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
 * @date 2024-09-10 10:27:07
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
     * 合约产品状态
     */
    @JacksonXmlProperty(
            localName = "PdctSts"
    )
    @Pattern(regexp = "SCPS01||SCPS02||SCPS03||SCPS04")
    private String pdctSts;
}


