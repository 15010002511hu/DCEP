package com.dcep.dips.wholesalepayment.dto.dc112;

import com.dcep.common.model.soap.FinInstnId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--DebtorAgent
 * @Author zhaotianwu
 * @date 2025-10-13 16:55:51
 */
@Data
public class DbtrAgt implements Serializable {
    /**
     * 付款方开户机构
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    @Valid
    private FinInstnId finInstnId;
}
