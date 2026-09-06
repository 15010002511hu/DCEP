package com.dcep.supergw.dto.dc059;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 合约应答
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:21
 */
@Data
public class CtrctRspn implements Serializable {
    /**
     * 合约返回码
     */
    @JacksonXmlProperty(
            localName = "CtrctPrcCd"
    )
    @Length(
            min = 1,
            max = 8
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ctrctPrcCd;

    /**
     * 合约返回信息
     */
    @JacksonXmlProperty(
            localName = "CtrctPrcInf"
    )
    private String ctrctPrcInf;
}
