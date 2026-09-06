package com.dcep.supergw.dto.dc052;

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
 * @date 2024-11-15 16:32:01
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
            regexp = "R353||R383||R384||R385||R365||R386"
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
