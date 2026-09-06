package com.dcep.dips.wholesalepayment.validation.validator;

import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.validation.CheckClrBatId;
import com.dcep.infocache.utils.BatchConfigUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author chenxf
 * @description : 交易转接报文批次号BatId校验
 *
 */
public class CheckClrBatIdValidaor implements ConstraintValidator<CheckClrBatId, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		ClearingDTO clearingDTO = (ClearingDTO) value;
		if (StringUtils.isBlank(clearingDTO.clrBatId())) {
			return true;
		}
		return BatchConfigUtil.compareBatchId(clearingDTO.clrCreDtTm(), clearingDTO.clrBatId());
	}

}
