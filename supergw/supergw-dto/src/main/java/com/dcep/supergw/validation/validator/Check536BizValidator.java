package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc536.Dcep53600101DTO;
import com.dcep.supergw.validation.Check536Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Check536BizValidator implements ConstraintValidator<Check536Biz, Object> {

	private static final String FAILE = "PR01";
	private static final String SUCCESS = "PR00";

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (!(value instanceof Dcep53600101DTO)) {
			return true;
		}
		Dcep53600101DTO dto = (Dcep53600101DTO) value;

		if (SUCCESS.equals(dto.getBizQryRef().getQryRs()) && dto.getCardInf() == null) {
			CheckUtils.changeValidatorMsg("当查询处理状态为PR00时,银行卡信息必填", context);
			return false;
		}

		if (FAILE.equals(dto.getBizQryRef().getQryRs()) && dto.getOprlErr() == null) {
			CheckUtils.changeValidatorMsg("当查询处理状态为PR01时,应答拒绝信息必填", context);
			return false;
		}

		return true;
	}
}
