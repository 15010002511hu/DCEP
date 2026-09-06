package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc310.Dcep31000101DTO;
import com.dcep.supergw.validation.Check310Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : duzhong
 * @version : Check310BizValidator.java v 0.1 2021-04-15
 * @description :
 */

public class Check310BizValidator implements ConstraintValidator<Check310Biz, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Dcep31000101DTO dto = (Dcep31000101DTO) value;

		if (("PR01".equals(dto.getRspsnInf().getRspsnSts()) && (dto.getRspsnInf().getRjctCd() == null))) {
			CheckUtils.changeValidatorMsg(
					"当业务回执状态为“PR01”时必填RejectCode",
					context);
			return false;
		}
		if (("PR02".equals(dto.getRspsnInf().getRspsnSts())
				&& ((dto.getRspsnInf().getRefTm() == null) || (dto.getRspsnInf().getRefNo() == null)))) {
			CheckUtils.changeValidatorMsg(
					"当业务回执状态为“PR02”时必填RefundTime,RefundNo",
					context);
			return false;
		}
		
		return true;
	}
}
