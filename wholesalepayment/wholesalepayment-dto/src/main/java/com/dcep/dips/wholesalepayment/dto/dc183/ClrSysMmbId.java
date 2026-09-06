package com.dcep.dips.wholesalepayment.dto.dc183;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件------ClearingSystemMemberIdentification
 * @Author luteng
 * @date 2025-09-05 16:39:39
 */
@Data
public class ClrSysMmbId implements Serializable {
    /**
     * 发起参与机构编码
     */
    @JacksonXmlProperty(
            localName = "MmbId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    private String mmbId;
}
