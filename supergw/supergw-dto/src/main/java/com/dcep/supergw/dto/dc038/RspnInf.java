package com.dcep.supergw.dto.dc038;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件ResponseInformation
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:20:05
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
            regexp = "R374||R356||R375||R377||R370"
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
     * 合约签约记录
     */
    @JacksonXmlProperty(
            localName = "CtrctSgntrRcrd"
    )
    @Valid
    private CtrctSgntrRcrd ctrctSgntrRcrd;
}
