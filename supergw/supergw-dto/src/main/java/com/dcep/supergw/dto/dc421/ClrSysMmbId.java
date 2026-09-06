package com.dcep.supergw.dto.dc421;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 组件------------------ClearingSystemMemberIdentification
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:36
 */
@Data
public class ClrSysMmbId implements Serializable {
    /**
     * 原发起参与机构
     */
    @JacksonXmlProperty(
            localName = "MmbId"
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
    private String mmbId;
}
