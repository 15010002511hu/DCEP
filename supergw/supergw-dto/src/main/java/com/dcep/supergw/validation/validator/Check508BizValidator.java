package com.dcep.supergw.validation.validator;

import com.dcep.supergw.dto.dc508.Dcep50800101DTO;
import com.dcep.supergw.validation.Check508Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : duzhong
 * @version : Check508BizValidator.java v 0.1 2020-11-17
 * @description :
 */
public class Check508BizValidator implements ConstraintValidator<Check508Biz, Object> {

	/**
	 * 当管理类型为MT01且业务回执状态为PR00时填写动态关联码，用于关联身份认证和身份确认的唯一标识
	 */
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Dcep50800101DTO dto = (Dcep50800101DTO) value;
		boolean result = true;

		if ("PR00".equals(dto.getRspsInf().getRspsnSts()) && "MT02".equals(dto.getRspsInf().getMgmtTp())) {
			result = result && (dto.getRspsInf().getPtcId() != null);
		}

		if ("PR00".equals(dto.getRspsInf().getRspsnSts()) && "MT01".equals(dto.getRspsInf().getMgmtTp())) {
			result = result && (dto.getRspsInf().getMsgSndCd() != null) && (dto.getRspsInf().getExstFlg() != null);

		}

		return result;

	}
}
