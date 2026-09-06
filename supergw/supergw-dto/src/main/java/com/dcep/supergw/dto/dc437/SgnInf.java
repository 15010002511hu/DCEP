package com.dcep.supergw.dto.dc437;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
/**
* @param
* @return
* @Author weiqianjing
* @date 2021/9/6
* @Dscription  代码优化，修改IDTp字段的长度和命名
*/
@Getter
@Setter
@ToString
public class SgnInf implements Serializable, DataEncryption {

	/**
	 * 
	 */
	private static final long serialVersionUID = -315114486526753916L;

	/**
	 * 签约人银行账户所属运营机构
	 */
	@JacksonXmlProperty(localName = "SgnAcctPtyId")
	@NotBlank
	@Length(min = 1, max = 14)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String sgnAcctPtyId;

	/**
	 * 签约人银行账户类型
	 */
	@JacksonXmlProperty(localName = "SgnAcctTp")
	@NotBlank
	@Length(min = 4, max = 4) 
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "AT00||AT01||AT02||AT03")
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
	 * 签约人银行账户户名
	 */
	@JacksonXmlProperty(localName = "SgnAcctNm")
	@NotBlank
	@Length(min = 1, max = 240)
	private String sgnAcctNm;

	/**
	 * 签约人证件类型
	 */
	@JacksonXmlProperty(localName = "IDTp")
	@Length(min = 4, max = 4)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "IT01||IT02||IT03||IT04||IT05||IT06||IT07||IT08||IT09||IT10||IT11||IT12||IT13||IT14||IT99")
	private String idTp;

	/**
	 * 签约人证件号码
	 */
	@JacksonXmlProperty(localName = "IDNo")
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String idNo;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if (this.getSgnAcctId() != null) {
			this.setSgnAcctId(encryptionHelper.encrypt(this.getSgnAcctId()));
		}
		if (this.getSgnAcctNm() != null) {
			this.setSgnAcctNm(encryptionHelper.encrypt(this.getSgnAcctNm()));
		}
		if (this.getIdNo() != null) {
			this.setIdNo(encryptionHelper.encrypt(this.getIdNo()));
		}

	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if (this.getSgnAcctId() != null) {
			this.setSgnAcctId(encryptionHelper.decrypt(this.getSgnAcctId()));
		}
		if (this.getSgnAcctNm() != null) {
			this.setSgnAcctNm(encryptionHelper.decrypt(this.getSgnAcctNm()));
		}
		if (this.getIdNo() != null) {
			this.setIdNo(encryptionHelper.decrypt(this.getIdNo()));
		}
	}
}
