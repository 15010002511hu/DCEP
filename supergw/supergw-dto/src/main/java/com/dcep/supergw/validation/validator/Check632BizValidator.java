package com.dcep.supergw.validation.validator;

import com.dcep.supergw.dto.dc632.Dcep63200101DTO;
import com.dcep.supergw.validation.Check632Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : duzhong
 * @version : Check605BizValidator.java v 0.1 2020-11-17
 * @description :
 */
public class Check632BizValidator implements ConstraintValidator<Check632Biz, Object> {
	/**
	 * HardwareWalletUpdTp为01时必须存在ContextId
	 */
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Dcep63200101DTO dto = (Dcep63200101DTO) value;
		if (dto.getRspsnInf().getTransactionStatusList().size() > 100) {
			return false;
		}
		return true;
	}
}
