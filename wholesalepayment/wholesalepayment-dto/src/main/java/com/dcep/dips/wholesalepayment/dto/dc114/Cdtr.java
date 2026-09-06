package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件----Creditor
 * @Author zhaotianwu
 * @date 2025-10-13 17:13:44
 */
@Data
public class Cdtr implements Serializable {
    /**
     * 收款直接参与机构
     */
    @JacksonXmlProperty(
            localName = "Agt"
    )
    @NotNull
    @Valid
    private Agt agt;
}
