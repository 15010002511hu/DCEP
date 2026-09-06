package com.dcep.supergw.validation.validator;

import com.dcep.supergw.dto.dc415.Dcep41500101DTO;
import com.dcep.supergw.validation.Check415Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : duzhong
 * @version : Check415BizValidator.java v 0.1 2020-11-17
 * @description :
 */
public class Check415BizValidator implements ConstraintValidator<Check415Biz, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Dcep41500101DTO dto = (Dcep41500101DTO) value;

		String rs = dto.getTrxInf().getTrxTp();
		if ("TT03".equals(rs)) {
			return (dto.getMrchntInf() != null) && (dto.getQrcodeInf() == null);
		} else if ("TT00".equals(rs) || "TT01".equals(rs) || "TT02".equals(rs)) {
			return (dto.getMrchntInf() == null) && (dto.getQrcodeInf() != null);
		}
		return true;
	}
}
