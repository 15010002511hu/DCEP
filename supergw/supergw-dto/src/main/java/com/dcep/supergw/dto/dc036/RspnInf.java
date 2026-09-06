package com.dcep.supergw.dto.dc036;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件ResponseInformation
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:57:57
 */
@Data
public class RspnInf implements Serializable {
    /**
     * 业务回执状态
     */
    @JacksonXmlProperty(
            localName = "RspnSts"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String rspnSts;

    /**
     * 业务拒绝码
     */
    @JacksonXmlProperty(
            localName = "RjctCd"
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    @Pattern(
            regexp = "R372||R373"
    )
    private String rjctCd;

    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(
            localName = "RjctInf"
    )
    @Length(
            min = 1,
            max = 105
    )
    private String rjctInf;

    /**
     * 签约方信息验证结果
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "ChckRspnInf")
    @Valid
    private List<ChckRspnInf> chckRspnInf;
}
