package com.dcep.supergw.dto.dc069;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
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

@Data
public class NtcInf implements Serializable {

    @NotBlank
    @Pattern(regexp = "SCOCT[0-9]{2}")
    @JacksonXmlProperty(localName = "NtcTp")
    String ntcTp;

    @NotBlank
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "CtrctSgntrRcrdId")
    String ctrctSgntrRcrdId;

    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "OprInstnId")
    String oprInstnId;

    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SvcInstnId")
    String svcInstnId;

    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "SgntrPtyId")
    String sgntrPtyId;

    @NotBlank
    @Length(min = 1, max = 16)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "PdctId")
    String pdctId;

    @Length(min = 1, max = 11)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "PdctVrsn")
    String pdctVrsn;

    @Length(min = 1, max = 11)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "TgPdctVrsn")
    String tgPdctVrsn;

    @NotNull
    @Valid
    @JacksonXmlProperty(localName = "CtrctCallParam")
    CtrctCallParam ctrctCallParam;

    @Valid
    @JacksonXmlProperty(localName = "ArgmtTxt")
    ArgmtTxt argmtTxt;


    @NotBlank(groups = Priority.Highest.class)
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    @JacksonXmlProperty(localName = "Ddln")
    String ddln;

    @NotNull
    @Size(min = 2, max = 99)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "SgntrPtyList")
    List<SgntrPtyList> sgntrPtyList;

    @Size(max = 99)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "RmngFndList")
    List<RmngFndList> rmngFndList;


}
