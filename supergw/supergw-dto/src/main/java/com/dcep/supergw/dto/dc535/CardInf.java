package com.dcep.supergw.dto.dc535;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Getter
@Setter
@ToString
public class CardInf  implements Serializable, DataEncryption {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7306727221309006466L;

	/**
     *签约协议号
     */
    @JacksonXmlProperty(localName = "SgnNo")
    @NotBlank
    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String sgnNo;
    
	/**
     * 签约人银行账户类型
     */
    @JacksonXmlProperty(localName = "SgnAcctTp")
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "AT00||AT01||AT02||AT03||AT04||AT05||AT06||AT07")
    private String sgnAcctTp;
    
	/**
     * 签约人银行账户账号
     */
    @JacksonXmlProperty(localName = "SgnAcctId")
    @NotBlank
    @Length(min = 1, max = 68)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String sgnAcctId;
	
	/**
     * 交易金额
     * 修改说明：2021-09-24：新架构三期交易金额由选输改为必输  by:weiqianjing
     */
    @Valid
    @JacksonXmlProperty(localName = "TrxAmt")
    @NotNull
    private ActiveCurrencyAndAmount trxAmt;
    
    
    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        if(this.getSgnAcctId() != null){
            this.setSgnAcctId(encryptionHelper.encrypt(this.getSgnAcctId()));
        }
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        if(this.getSgnAcctId() != null){
            this.setSgnAcctId(encryptionHelper.decrypt(this.getSgnAcctId()));
        }
    }
    
}
