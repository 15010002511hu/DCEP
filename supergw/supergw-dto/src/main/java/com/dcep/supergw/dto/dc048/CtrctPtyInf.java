package com.dcep.supergw.dto.dc048;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 签约方机构信息
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:38:54
 */
@Data
public class CtrctPtyInf implements Serializable {
    /**
     * 签约方机构类型
     */
    @JacksonXmlProperty(
            localName = "CtrctPtyTp"
    )
    @NotBlank
    @Pattern(
            regexp = "IST[0-9]{2}"
    )
    private String ctrctPtyTp;

    /**
     * 签约方一级机构编码
     */
    @JacksonXmlProperty(
            localName = "CtrctPtyId"
    )
    @Length(
            min = 1,
            max = 14
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    @NotBlank
    private String ctrctPtyId;

    /**
     * 签约方一级机构名称
     */
    @JacksonXmlProperty(
            localName = "CtrctPtyNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    @NotBlank
    private String ctrctPtyNm;

    /**
     * 签约方二级机构编码
     */
    @JacksonXmlProperty(
            localName = "CtrctSubPtyId"
    )
    @Length(
            min = 1,
            max = 32
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ctrctSubPtyId;

    /**
     * 签约方二级机构名称
     */
    @JacksonXmlProperty(
            localName = "CtrctSubPtyNm"
    )
    @Length(
            min = 1,
            max = 60
    )
    private String ctrctSubPtyNm;
}
