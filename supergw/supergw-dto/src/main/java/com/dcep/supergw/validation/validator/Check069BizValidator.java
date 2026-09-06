package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc069.Dcep06900101DTO;
import com.dcep.supergw.validation.Check069Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class Check069BizValidator implements ConstraintValidator<Check069Biz, Object> {

    private static final String SCOCT01 = "SCOCT01";

    private static final String SCOCT04 = "SCOCT04";

    /**
     * PR01
     */
    private static final String SCOCT02 = "SCOCT02";


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (!(value instanceof Dcep06900101DTO)) {
            return true;
        }

        Dcep06900101DTO dto = (Dcep06900101DTO) value;
        //当NtcTp为SCOCT02时，TgtPdctVrsn必填
        if (SCOCT02.equals(dto.getNtcInf().getNtcTp())) {
            if (StringUtils.isBlank(dto.getNtcInf().getTgPdctVrsn())) {
                CheckUtils.changeValidatorMsg("当合约通知类型NtcTp为SCOCT02时，目标产品版本号TgPdctVrsn必填", context);
                return false;
            }
        }

        if (SCOCT04.equals(dto.getNtcInf().getNtcTp())) {
            if (dto.getNtcInf().getRmngFndList()==null|| dto.getNtcInf().getRmngFndList().isEmpty()) {
                CheckUtils.changeValidatorMsg("当合约通知类型NtcTp为SCOCT04时，剩余资金去向列表必填", context);
                return false;
            }
        }
        return true;
    }
}
