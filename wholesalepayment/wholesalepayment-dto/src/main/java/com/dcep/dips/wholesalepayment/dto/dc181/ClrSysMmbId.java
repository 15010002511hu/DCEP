package com.dcep.dips.wholesalepayment.dto.dc181;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件------ClearingSystemMemberIdentification
 * @Author luteng
 * @date 2025-09-05 16:32:04
 */
@Data
public class ClrSysMmbId implements Serializable {
    /**
     * 参与机构
     */
    @JacksonXmlProperty(localName = "MmbId")
    @Length(min = 1, max = 35)
    @NotBlank
    private String mmbId;
}
