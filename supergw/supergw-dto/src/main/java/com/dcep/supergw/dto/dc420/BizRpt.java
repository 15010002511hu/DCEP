package com.dcep.supergw.dto.dc420;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
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

/**
 * 应答的原业务信息
 * @author duzhong
 */
@Getter
@Setter
@ToString
public class BizRpt implements Serializable, DataEncryption {
    /**  */
    private static final long serialVersionUID = -4399682892586463732L;

    /**
     * 原交易状态
     */
    @JacksonXmlProperty(localName = "TrnRs")
    @NotBlank
    @Length(min = 1, max = 4)
    @Pattern(regexp = "PR00||PR01||PR02")
    private String trnRs;
    /**
     * 原交易原因
     */
    @JacksonXmlProperty(localName = "Rsn")
    @Valid
    private Rsn rsn;
    /**
     * 查询处理状态
     */
    @JacksonXmlProperty(localName = "OrgnlTxInf")
    @NotNull
    @Valid
    private OrgnlTxInf orgnlTxInf;

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
    	if(this.orgnlTxInf != null){
            this.orgnlTxInf.encryptData(encryptionHelper);
        }
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
    	if(this.orgnlTxInf != null){
            this.orgnlTxInf.decryptData(encryptionHelper);
        }
    }
}
