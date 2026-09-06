package com.dcep.supergw.validation.validator;

import com.dcep.supergw.dto.dc507.Dcep50700101DTO;
import com.dcep.supergw.validation.Check507Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : duzhong
 * @version : Check507BizValidator.java v 0.1 2020-11-17
 * @description :
 */
public class Check507BizValidator implements ConstraintValidator<Check507Biz, Object> {

	/**
	 * 当管理类型为MT01且业务回执状态为PR00时填写动态关联码，用于关联身份认证和身份确认的唯一标识
	 */
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Dcep50700101DTO dto = (Dcep50700101DTO) value;
		if ("MT02".equals(dto.getMgmtTp())) {
			return dto.getPtcInf().getWltShrtId() != null && dto.getPtcInf().getPtcId() != null
					&& dto.getPtcInf().getMsgSndCd() != null && dto.getPtcInf().getVrfyTp() != null
					&& dto.getPtcInf().getMsgVrfy() != null;
		}

		return true;
	}
}
