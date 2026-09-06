package com.dcep.dips.wholesalepayment.dto.dc261;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 用户移动终端信息
 *
 * @author caoxiaogai
 *
 */
@JacksonXmlRootElement(localName = "UserTerInf")
@Setter
@Getter
@ToString
public class UserTerInf implements Serializable {

    private static final long serialVersionUID = 1754171405056311192L;

    /**
     * 订单号
     */
    @JacksonXmlProperty(localName = "TrxDevcInf")
    @Length(min = 1, max = 149)
    private String trxDevcInf;
}
