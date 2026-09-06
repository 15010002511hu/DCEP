package com.dcep.supergw.dto.dc059;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 发起方预言机
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:25
 */
@Data
public class SndrOracle implements Serializable {
    /**
     * 预言机服务机构Id
     */
    @JacksonXmlProperty(
            localName = "SndrOracleSvcPtyId"
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
    private String sndrOracleSvcPtyId;

    /**
     * 预言机Id
     */
    @JacksonXmlProperty(
            localName = "SndrOracleId"
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
    private String sndrOracleId;
}
