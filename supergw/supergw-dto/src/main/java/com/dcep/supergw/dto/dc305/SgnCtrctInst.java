package com.dcep.supergw.dto.dc305;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 签约人合约实例信息
 *
 * @Author qinchaoyong
 * @date 2024-10-23 16:26:57
 */
@Data
public class SgnCtrctInst implements Serializable {
    /**
     * 合约实例ID
     */
    @JacksonXmlProperty(
            localName = "CtrctId"
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
    private String ctrctId;

    /**
     * 合约产品ID
     */
    @JacksonXmlProperty(
            localName = "PdctId"
    )
    @Length(
            min = 1,
            max = 64
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String pdctId;

    /**
     * 合约参数
     */
    @JacksonXmlProperty(
            localName = "CtrctParam"
    )
    @Valid
    private CtrctParam ctrctParam;
}
