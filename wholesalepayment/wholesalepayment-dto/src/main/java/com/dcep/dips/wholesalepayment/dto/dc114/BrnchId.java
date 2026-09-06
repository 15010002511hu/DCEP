package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 组件----BranchIdentification
 * @Author zhaotianwu
 * @date 2025-10-13 17:14:00
 */
@Data
public class BrnchId implements Serializable {
    /**
     * 发起间接参与机构
     */
    @JacksonXmlProperty(
            localName = "Id"
    )
    @Length(
            min = 1,
            max = 35
    )
    private String id;
}
