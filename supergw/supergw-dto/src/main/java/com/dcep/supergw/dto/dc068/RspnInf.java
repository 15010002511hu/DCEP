package com.dcep.supergw.dto.dc068;

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
 * @date 2025-03-03 10:40:19
 */
@Data
public class RspnInf implements Serializable {
    /**
     * 业务回执状态
     */
    @JacksonXmlProperty(
            localName = "RspnSts"
    )
    @Pattern(regexp = "PR[0-9]{2}")
    @NotBlank
    private String rspnSts;

    /**
     * 业务拒绝码
     */
    @JacksonXmlProperty(
            localName = "RjctCd"
    )
    @Pattern(regexp = "R[0-9]{3}")
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
