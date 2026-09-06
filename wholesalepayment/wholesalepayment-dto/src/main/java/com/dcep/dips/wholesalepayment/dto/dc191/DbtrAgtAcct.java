package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 付款方代理账户
 * @Author qiaopengyu
 * @date 2025-10-30 21:16:59
 */
@Data
public class DbtrAgtAcct implements Serializable {
    /**
     * 组件----Identification
     */
    @JacksonXmlProperty(
            localName = "Id"
    )
    @NotNull
    @Valid
    private Id id;

    /**
     * 付款方代理钱包名称
     */
    @JacksonXmlProperty(
            localName = "Nm"
    )
    @Length(
            min = 1,
            max = 70
    )
    private String nm;
}
