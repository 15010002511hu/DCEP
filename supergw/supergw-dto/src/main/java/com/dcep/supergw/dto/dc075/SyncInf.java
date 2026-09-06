package com.dcep.supergw.dto.dc075;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SyncInf implements Serializable {

    @NotNull
    @Valid
    @JacksonXmlProperty(localName = "SyncFileInf")
    SyncFileInf syncFileInf;

    @Valid
    @JacksonXmlProperty(localName = "DtlFileInf")
    DtlFileInf dtlFileInf;


}
