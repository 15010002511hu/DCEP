package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc304.Dcep30400101DTO;
import com.dcep.supergw.validation.Check304Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : duzhong
 * @version : Check304BizValidator.java v 0.1 2021-04-15
 * @description :
 */

public class Check304BizValidator implements ConstraintValidator<Check304Biz, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Dcep30400101DTO dto = (Dcep30400101DTO) value;

		if (("PR01".equals(dto.getRspsnInf().getRspsnSts()) && (dto.getRspsnInf().getRjctCd() == null))) {
			return false;
		}
		if (("PR02".equals(dto.getRspsnInf().getRspsnSts())
				&& ((dto.getRspsnInf().getOrdrTm() == null) || (dto.getRspsnInf().getOrdrTm() == null)))) {
			return false;
		}
		CheckUtils.changeValidatorMsg(
				"当业务回执状态为“PR01”时必填RejectCode;当业务回执状态为“PR02”时必填OrderTime;当业务回执状态为“PR02”时必填OrderNumber",
				context);
		
		return true;
	}
}
