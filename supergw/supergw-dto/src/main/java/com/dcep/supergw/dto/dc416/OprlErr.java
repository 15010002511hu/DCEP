package com.dcep.supergw.dto.dc416;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 应答拒绝信息
 * @author chenkai
 * @version $Id: OprlErr.java, v 0.1 2019年7月17日 上午11:32:47  chenkai  Exp $
 */
@Getter
@Setter
@ToString
public class OprlErr implements Serializable {
    /**  */
    private static final long serialVersionUID = -2388999489826540340L;
    /**
     * 应答拒绝信息err
     */
    @JacksonXmlProperty(localName = "Err")
    @Valid
    private Err               err;


    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(localName = "RjctInf")
    @Length(min = 1,max = 105)
    private String            rjctInf;

}
