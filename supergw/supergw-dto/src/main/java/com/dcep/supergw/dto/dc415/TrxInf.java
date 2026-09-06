package com.dcep.supergw.dto.dc415;

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
 * 交易查询信息
 * @author chenkai
 */
@JacksonXmlRootElement(localName = "TrxInf")
@Getter
@Setter
@ToString
public class TrxInf implements Serializable {


    private static final long serialVersionUID = -3728130312164419871L;


    /**
     * 交易批次号
     */
    @JacksonXmlProperty(localName = "BatchId")
    @Length(min = 1, max = 13)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  batchId;

    /**
     * 交易类型
     * TT00: 普通汇款
     * TT01: 扫码支付
     * TT02: 碰一碰支付
     */
    @JacksonXmlProperty(localName = "TrxTp")
    @NotBlank
    @Pattern(regexp = "TT00||TT01||TT02||TT03")
    private String                  trxTp;


}
