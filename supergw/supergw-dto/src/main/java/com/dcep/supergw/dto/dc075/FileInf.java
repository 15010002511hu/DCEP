package com.dcep.supergw.dto.dc075;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class FileInf implements Serializable {

    @NotBlank
    @Length(min = 1,max = 64)
    @JacksonXmlProperty(localName = "FilePath")
    String filePath;


    @JacksonXmlElementWrapper(localName = "fileNameList")
    @JacksonXmlProperty(localName = "FileName")
    List<String> fileNameList;
}
