package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * 参与机构信息
 * @Author luteng
 * @date 2025-10-12 16:17:04
 */
@Data
public class Ownr implements Serializable {
    /**
     * 组件------Identification
     */
    @JacksonXmlProperty(
            localName = "Id"
    )
    @Valid
    private IdOfOrgId id;
}
