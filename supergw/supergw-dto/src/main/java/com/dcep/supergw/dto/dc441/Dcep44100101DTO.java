package com.dcep.supergw.dto.dc441;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check441Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 对公钱包绑定账户申请报文<dcep.441.001.01>
 * 
 * @author duzhong
 */
@JacksonXmlRootElement(localName = "BndngAcctPtcMgmtReq", namespace = "http://www.dcep.com/dcep/44100101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.441.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@Check441Biz(groups = Priority.Lowest.class)
public class Dcep44100101DTO extends GwDTO implements DataEncryption {

	private static final long serialVersionUID = -7231835772911811423L;

	/**
	 * 【业务头组件】
	 */
	@JacksonXmlProperty(localName = "GrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@CheckGrpHdrOrgId(groups = Priority.Lowest.class)
	@CheckGrpHdrMsgId(groups = Priority.Lowest.class)
	@Valid
	private GrpHdr grpHdr;

	@JacksonXmlProperty(localName = "MgmtTp")
	@NotBlank
	@Length(min = 1, max = 240)
	@Pattern(regexp = "MT03||MT05||MT06")
	private String mgmtTp;

	/**
	 * 【应答的原业务信息】
	 */
	@JacksonXmlProperty(localName = "PtcInf")
	@NotNull
	@Valid
	private PtcInf ptcInf;

	@Override
	public void init() {

	}

	@Override
	public String fetchMsgId() {
		return grpHdr.getMsgId();
	}

	@Override

	public boolean check(SoapHeader header) {

		return CheckUtils.requestMsgChk(header, grpHdr);

	}

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		List<String> plainTextList =  fetchSecretFactor();
		List<String> cipherTextList =  encryptionHelper.encrypt(plainTextList);
		secretAssign(cipherTextList);
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		List<String> cipherTextList =  fetchSecretFactor();
		List<String> plainTextList =  encryptionHelper.decrypt(cipherTextList);
		secretAssign(plainTextList);
	}

	public List<String> fetchSecretFactor() {
		List<String> result = new ArrayList<>();
		if (this.ptcInf != null) {
			if (this.ptcInf.getSgnAcctId() != null) {
				result.add(this.ptcInf.getSgnAcctId());
			} else {
				result.add(null);
			}
			if (this.ptcInf.getSgnAcctNm() != null) {
				result.add(this.ptcInf.getSgnAcctNm());
			} else {
				result.add(null);
			}
			if (this.ptcInf.getLglRepNm() != null) {
				result.add(this.ptcInf.getLglRepNm());
			} else {
				result.add(null);
			}
			if (this.ptcInf.getLglRepIDNo() != null) {
				result.add(this.ptcInf.getLglRepIDNo());
			} else {
				result.add(null);
			}
			if (this.ptcInf.getTel() != null) {
				result.add(this.ptcInf.getTel());
			} else {
				result.add(null);
			}
			if (this.ptcInf.getWltId() != null) {
				result.add(this.ptcInf.getWltId());
			} else {
				result.add(null);
			}
		}
		return result;
	}

	public void secretAssign(List<String> secretList) {
		if (this.ptcInf != null) {
			if (this.ptcInf.getSgnAcctId() != null) {
				this.ptcInf.setSgnAcctId(secretList.get(0));
			}
			if (this.ptcInf.getSgnAcctNm() != null) {
				this.ptcInf.setSgnAcctNm(secretList.get(1));
			}
			if (this.ptcInf.getLglRepNm() != null) {
				this.ptcInf.setLglRepNm(secretList.get(2));
			}
			if (this.ptcInf.getLglRepIDNo() != null) {
				this.ptcInf.setLglRepIDNo(secretList.get(3));
			}
			if (this.ptcInf.getTel() != null) {
				this.ptcInf.setTel(secretList.get(4));
			}
			if (this.ptcInf.getWltId() != null) {
				this.ptcInf.setWltId(secretList.get(5));
			}
		}
	}
}