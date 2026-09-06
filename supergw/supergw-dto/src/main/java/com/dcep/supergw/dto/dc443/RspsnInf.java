package com.dcep.supergw.dto.dc443;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author chenkai
 */
@JacksonXmlRootElement(localName = "RspsnInf")
@Getter
@Setter
@ToString
public class RspsnInf implements Serializable {

    private static final long serialVersionUID = -8091139919699632899L;


    /**
     * 业务回执状态
     * PR00：成功
     * PR01：失败
     * PR02：处理中
     * PR03：推定成功
     * PR04：推定失败
     */
    @JacksonXmlProperty(localName = "RspsnSts")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "PR00||PR01||PR02||PR03||PR04")
    private String                  rspsnSts;

    /**
     * 业务拒绝码
     */
    @JacksonXmlProperty(localName = "RjctCd")
    @Pattern(regexp = "R[0-9]{3}")
    private String                  rjctCd;

    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(localName = "RjctInf")
    @Length(min = 1, max = 105)
    private String                  rjctInf;



}
