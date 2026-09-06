package com.dcep.supergw.dto.dc075;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DtlFileInf implements Serializable {

    @NotBlank
    @Length(min = 1, max = 10)
    @Pattern(regexp = "^\\d+$", message = "总条数只能是数字")
    @JacksonXmlProperty(localName = "FileInfNb")
    String fileInfNb;


    @Valid
    @JacksonXmlElementWrapper(localName = "FileInfList")
    @JacksonXmlProperty(localName = "FileInf")
    List<FileInf> fileInf;

}
