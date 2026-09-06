package com.dcep.supergw.dto.dc305;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 合约参数
 *
 * @Author qinchaoyong
 * @date 2024-10-23 16:26:57
 */
@Data
public class CtrctParam implements Serializable {
    /**
     * 调用路径
     */
    @JacksonXmlProperty(
            localName = "InvokePth"
    )
    @Length(
            min = 1,
            max = 64
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String invokePth;

    /**
     * 调用参数
     */
    @JacksonXmlProperty(
            localName = "InvokeParam"
    )
    private String invokeParam;
}
