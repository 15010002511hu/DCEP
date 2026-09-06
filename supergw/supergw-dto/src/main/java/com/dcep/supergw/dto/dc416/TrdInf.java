package com.dcep.supergw.dto.dc416;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 交易信息
 * @author chenkai
 */
@Getter
@Setter
@ToString
public class TrdInf implements Serializable {

    private static final long       serialVersionUID = 1927999575204723960L;

    /**
     * 交易类型
     * TT00: 普通汇款
     * TT01: 扫码支付
     * TT02: 碰一碰支付
     */
    @JacksonXmlProperty(localName = "TrxTp")
    @Length(min=4,max = 4)
    @NotBlank
    @Pattern(regexp = "TT00||TT01||TT02||TT03")
    private String                  trxTp;

    /**
     * 金额
     */
    @JacksonXmlProperty(localName = "TrxAmt")
    @Valid
    private ActiveCurrencyAndAmount trxAmt;

    /**
     * 业务类型编码
     */
    @JacksonXmlProperty(localName = "TrxBizTp")
    @NotBlank
    @Length(min = 1, max = 4)
    private String                  trxBizTp;

    /**
     * 业务种类编码
     */
    @JacksonXmlProperty(localName = "TrxCtgyCd")
    @NotBlank
    @Length(min = 1, max = 8)
    private String                  trxCtgyCd;

}
