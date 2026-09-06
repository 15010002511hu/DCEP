package com.dcep.supergw.dto.dc059;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 发起方机构信息
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:25
 */
@Data
public class SndrPtyInf implements Serializable {
    /**
     * 发起方机构类型
     */
    @JacksonXmlProperty(
            localName = "SndrPtyTp"
    )
    @NotBlank
    @Pattern(
            regexp = "IST[0-9]{2}"
    )
    private String sndrPtyTp;

    /**
     * 发起方一级机构编码
     */
    @JacksonXmlProperty(
            localName = "SndrPtyId"
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
    private String sndrPtyId;

    /**
     * 发起方一级机构名称
     */
    @JacksonXmlProperty(
            localName = "SndrPtyNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    @NotBlank
    private String sndrPtyNm;

    /**
     * 发起方二级机构编码
     */
    @JacksonXmlProperty(
            localName = "SndrSubPtyId"
    )
    @Length(
            min = 1,
            max = 32
    )
    private String sndrSubPtyId;

    /**
     * 发起方二级机构名称
     */
    @JacksonXmlProperty(
            localName = "SndrSubPtyNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    private String sndrSubPtyNm;
}
