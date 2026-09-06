package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc323.Dcep32300101DTO;
import com.dcep.supergw.validation.Check323Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class Check323BizValidator implements ConstraintValidator<Check323Biz, Object> {

    private final static String TT01 = "TT01";
    private final static String TT02 = "TT02";
    private final static String TT14 = "TT14";
    private final static String TT15 = "TT15";
    private final static String QT04 = "QT04";
    private final static String MP01 = "MP01";
    private final static String MP02 = "MP02";
    private final static String MP03 = "MP03";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep32300101DTO)) {
            return true;
        }

        Dcep32300101DTO dto = (Dcep32300101DTO) value;

        if (dto.getTrxInf() != null && (TT01.equals(dto.getTrxInf().getTrxTp()) || TT02.equals(dto.getTrxInf().getTrxTp()))) {
            if (dto.getPmtChnlInf() == null || dto.getUserInf() == null || dto.getUserTerInf() == null || dto.getQrCodeInf() == null || dto.getPtcInf() == null) {
                CheckUtils.changeValidatorMsg("当交易类型为“TT01|TT02”时,PmtChnlInf、UserInf、UserTerInf、QrCodeInf、PtcInf必填", context);
                return false;
            }
            if (dto.getQrCodeInf() != null && QT04.equals(dto.getQrCodeInf().getQrCodeTp())) {
                if (dto.getOrdrInf() == null) {
                    CheckUtils.changeValidatorMsg("当交易类型为“TT01|TT02”且收款码类型为QT04时,OrdrInf必填", context);
                    return false;
                }
            }
        }
        if (dto.getTrxInf() != null && (TT14.equals(dto.getTrxInf().getTrxTp()) || TT15.equals(dto.getTrxInf().getTrxTp()))) {
            if (dto.getOrdrInf() == null) {
                CheckUtils.changeValidatorMsg("当交易类型为“TT14|TT15”时,OrdrInf必填", context);
                return false;
            }
            if (dto.getPmtCodeInf() == null) {
                CheckUtils.changeValidatorMsg("当交易类型为“TT14|TT15”时,PmtCodeInf必填", context);
                return false;
            }
        }
        if (dto.getMrchntInf() != null && (MP02.equals(dto.getMrchntInf().getMrchntPrprty()) || MP03.equals(dto.getMrchntInf().getMrchntPrprty()))) {
            if (dto.getMrchntTerInf() != null && (StringUtils.isBlank(dto.getMrchntTerInf().getPltfrmNm()))) {
                CheckUtils.changeValidatorMsg("当商户属性为“MP02|MP03”时,PltfrmNm必填", context);
                return false;
            }
        }
        if (dto.getMrchntInf() != null && (MP01.equals(dto.getMrchntInf().getMrchntPrprty()) || MP03.equals(dto.getMrchntInf().getMrchntPrprty()))) {
            if (dto.getMrchntTerInf() != null && (StringUtils.isBlank(dto.getMrchntTerInf().getMrchntBizAddr()))) {
                CheckUtils.changeValidatorMsg("当商户属性为“MP02|MP03”时,MrchntBizAddr必填", context);
                return false;
            }
        }
        return true;
    }
}