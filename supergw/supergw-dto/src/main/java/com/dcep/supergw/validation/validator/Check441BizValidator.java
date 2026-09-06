package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc441.Dcep44100101DTO;
import com.dcep.supergw.validation.Check441Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : duzhong
 * @version : Check441BizValidator.java v 0.1 2020-05-11
 * @description :
 */
public class Check441BizValidator implements ConstraintValidator<Check441Biz, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Dcep44100101DTO dto441 = (Dcep44100101DTO) value;
		if ("MT03".equalsIgnoreCase(dto441.getMgmtTp())) {
			if (StringUtils.isBlank(dto441.getPtcInf().getPtcId())
					||StringUtils.isBlank(dto441.getPtcInf().getSgnAcctTp())
					|| StringUtils.isBlank(dto441.getPtcInf().getSgnAcctId())
					|| StringUtils.isBlank(dto441.getPtcInf().getSgnAcctNm())
					|| StringUtils.isBlank(dto441.getPtcInf().getLglRepNm())
					|| StringUtils.isBlank(dto441.getPtcInf().getLglRepIDTp())
					|| StringUtils.isBlank(dto441.getPtcInf().getLglRepIDNo())
					|| StringUtils.isBlank(dto441.getPtcInf().getTel())
					|| StringUtils.isBlank(dto441.getPtcInf().getCorprtnNm())
					|| StringUtils.isBlank(dto441.getPtcInf().getCorprtnIDTp())
					|| StringUtils.isBlank(dto441.getPtcInf().getCorprtnIDNo())) {
				CheckUtils.changeValidatorMsg("管理类型为MT03时，签约协议号、单位银行账户类型、单位银行账户账号、单位银行账户户名、单位名称、单位证明文件类型、单位证明文件号码、法定代表人或单位负责人姓名、法定代表人或单位负责人证件类型、法定代表人或单位负责人证件号码、单位联系手机号码必须填写", context);
				return false;
			}

		}

		if ("MT05".equalsIgnoreCase(dto441.getMgmtTp())) {
			if (StringUtils.isBlank(dto441.getPtcInf().getCorprtnNm())
					||StringUtils.isBlank(dto441.getPtcInf().getCorprtnIDTp())
					||StringUtils.isBlank(dto441.getPtcInf().getCorprtnIDNo())) {
				CheckUtils.changeValidatorMsg(
						"管理类型为“MT05”时，单位名称、单位证明文件类型、单位证明文件号码必须填写。",
						context);
				return false;
			}
		}


		if ("MT06".equalsIgnoreCase(dto441.getMgmtTp())) {
			if (StringUtils.isBlank(dto441.getPtcInf().getSgnAcctTp())
					|| StringUtils.isBlank(dto441.getPtcInf().getSgnAcctId())
					|| StringUtils.isBlank(dto441.getPtcInf().getSgnAcctNm())
					|| StringUtils.isBlank(dto441.getPtcInf().getLglRepNm())
					|| StringUtils.isBlank(dto441.getPtcInf().getLglRepIDTp())
					|| StringUtils.isBlank(dto441.getPtcInf().getLglRepIDNo())
					|| StringUtils.isBlank(dto441.getPtcInf().getTel())
					|| StringUtils.isBlank(dto441.getPtcInf().getCorprtnNm())
					|| StringUtils.isBlank(dto441.getPtcInf().getCorprtnIDTp())
					|| StringUtils.isBlank(dto441.getPtcInf().getCorprtnIDNo())) {
				CheckUtils.changeValidatorMsg(
						"“普通签约”，管理类型填写“MT06-普通签约”时，“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、“单位名称”、“单位证明文件类型”、“单位证明文件号码”、“法定代表人或单位负责人姓名”、“法定代表人或单位负责人证件类型” 、“法定代表人或单位负责人证件号码” 、“单位联系手机号码”等域必须填写。",
						context);
				return false;
			}
		}
		return true;

	}
}
