package com.dcep.supergw.dto.dc048;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件ResponseInformation
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:39:01
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
            regexp = "R374||R356||R387||R377"
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
}
