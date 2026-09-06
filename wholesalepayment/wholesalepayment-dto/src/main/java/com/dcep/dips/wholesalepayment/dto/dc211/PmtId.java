package com.dcep.dips.wholesalepayment.dto.dc211;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * 
 * @author sunxiaofeng
 * @version $Id: PmtId.java, v 0.1 2019年10月15日 上午11:13:52 sunxiaofeng Exp $
 */
@JacksonXmlRootElement(localName = "PmtId")
@Getter
@Setter
@ToString
public class PmtId implements Serializable {
    /**  */
    private static final long serialVersionUID = -5402362809713619720L;
    /**
     *  交易批次号
     */
    @JacksonXmlProperty(localName = "InstrId")
    @NotBlank
    @Pattern(regexp = "^[B][0-9]{12}$")
    private String            instrId;
    /**
     * 端到端标识号
     */
    @JacksonXmlProperty(localName = "EndToEndId")
    @NotBlank
    @Length(min = 1, max = 35)
    private String            endToEndId;
    /**
     * 明细标识号
     */
    @JacksonXmlProperty(localName = "TxId")
    @NotBlank
    @Length(min = 32, max = 32)
    private String            txId;

}
