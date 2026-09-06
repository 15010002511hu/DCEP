package com.dcep.supergw.dto.dc013;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 变更请求信息
 *
 * @Author qinchaoyong
 * @date 2024-09-10 12:01:33
 */
@Data
public class ChngInf implements Serializable {
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
     * 合约产品状态变更类型
     */
    @JacksonXmlProperty(
            localName = "PdctStsChngTp"
    )
    @NotBlank
    @Pattern(regexp = "SCPSCT01||SCPSCT02||SCPSCT03||SCPSCT04||SCPSCT05")
    private String pdctStsChngTp;

    /**
     * 变更前状态
     */
    @JacksonXmlProperty(
            localName = "PdctStsBfrChng"
    )
    @NotBlank
    @Pattern(regexp = "SCPS01||SCPS02||SCPS03||SCPS04")
    private String pdctStsBfrChng;

    /**
     * 变更后状态
     */
    @JacksonXmlProperty(
            localName = "PdctStsAftrChng"
    )
    @NotBlank
    @Pattern(regexp = "SCPS01||SCPS02||SCPS03||SCPS04")
    private String pdctStsAftrChng;
}
