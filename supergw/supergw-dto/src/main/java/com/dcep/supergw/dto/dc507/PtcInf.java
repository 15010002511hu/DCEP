package com.dcep.supergw.dto.dc507;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * ProtocolInformation 协议信息
 * 
 * @author duz
 */
@JacksonXmlRootElement(localName = "PtcInf")
@Getter
@Setter
@ToString
public class PtcInf implements DataEncryption, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3390794958548298688L;

	/**
	 * 签约协议号
	 */
	@JacksonXmlProperty(localName = "PtcId")
	@Length(min = 1, max = 34)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String ptcId;

	/**
	 * 动态关联码 商户关联身份认证和身份确认的唯一标识
	 */
	@JacksonXmlProperty(localName = "MsgSndCd")
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String msgSndCd;

	/**
	 * 单位银行账户所属运营机构编码
	 */
	@JacksonXmlProperty(localName = "VrfyTp")
	@Pattern(regexp = "VT01||VT02||VT03")
	private String vrfyTp;

	/**
	 * 动态验证码
	 */
	@JacksonXmlProperty(localName = "MsgVrfy")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Length(min = 1, max = 128)
	private String msgVrfy;

	/**
	 * 手机号码
	 */
	@JacksonXmlProperty(localName = "MobPhNo")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Length(min = 1, max = 35)
	private String mobPhNo;

	/**
	 * 钱包ID辩识码
	 */
	@JacksonXmlProperty(localName = "WltShrtId")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	@Length(min = 4, max = 4)
	private String wltShrtId;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if (this.getMobPhNo() != null) {
			 this.setMobPhNo(encryptionHelper.encrypt(this.getMobPhNo()));
		}

	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if (this.getMobPhNo() != null) {
			this.setMobPhNo(encryptionHelper.decrypt(this.getMobPhNo()));
		}

	}

}
