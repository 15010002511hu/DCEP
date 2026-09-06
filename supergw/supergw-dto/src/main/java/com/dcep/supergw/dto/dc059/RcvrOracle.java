package com.dcep.supergw.dto.dc059;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 接收方预言机
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:21
 */
@Data
public class RcvrOracle implements Serializable {
    /**
     * 预言机服务机构Id
     */
    @JacksonXmlProperty(
            localName = "RcvrOracleSvcPtyId"
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
    private String rcvrOracleSvcPtyId;

    /**
     * 预言机Id
     */
    @JacksonXmlProperty(
            localName = "RcvrOracleId"
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
    private String rcvrOracleId;
}
