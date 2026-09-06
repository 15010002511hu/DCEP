package com.dcep.supergw.validation.validator;

import com.dcep.supergw.dto.dc631.Dcep63100101DTO;
import com.dcep.supergw.validation.Check631Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : duzhong
 * @version : Check605BizValidator.java v 0.1 2020-11-17
 * @description :
 */
public class Check631BizValidator implements ConstraintValidator<Check631Biz, Object> {
	/**
	 * HardwareWalletUpdTp为01时必须存在ContextId
	 */
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Dcep63100101DTO dto = (Dcep63100101DTO) value;
		if (dto.getTransactionList().size() > 100) {
			return false;
		}
		return true;
	}
}
