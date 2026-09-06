package com.dcep.supergw.dto.dc037;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 合约签约确认信息
 *
 * @Author qinchaoyong
 * @date 2024-11-26 14:16:10
 */
@Data
public class CnfrmInf implements Serializable {
    /**
     * 合约签约记录ID
     */
    @JacksonXmlProperty(
            localName = "CtrctSgntrRcrdId"
    )
    @Length(
            min = 1,
            max = 64
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    @NotBlank
    private String ctrctSgntrRcrdId;

    /**
     * 签约方运营机构
     */
    @JacksonXmlProperty(
            localName = "SgntrPtyId"
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
    private String sgntrPtyId;

    /**
     * 签约方列表
     */
    @JacksonXmlElementWrapper(
            useWrapping = false
    )
    @JacksonXmlProperty(
            localName = "SgntrPtyList"
    )
    @NotNull
    @Size(min = 2, max = 99)
    @Valid
    private List<SgntrPty> sgntrPtyList;
}
