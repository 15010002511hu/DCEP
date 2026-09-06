/*
 * pbcdci.cn Inc. Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc642;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 
 * @author liuqi
 * @version $Id: OprlErr.java, v 0.1 2019年8月24日 下午5:29:08 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "OprlErr")
@Getter
@Setter
@ToString
public class OprlErr implements Serializable {
    /**  */
    private static final long serialVersionUID = 1898546522700035629L;
    /**
     * Error
     */
    @JacksonXmlProperty(localName = "Err")
    @NotNull
    @Valid
    private Err err;

    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(localName = "RjctInf")
    @Length(min = 1, max = 105)
    private String rjctInf;

    public OprlErr() {}

    /**
     * @param err
     */
    public OprlErr(Err err, String rjctInf) {
        this.err = err;
        this.rjctInf = rjctInf;
    }

}
