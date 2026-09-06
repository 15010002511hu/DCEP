package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc301.Dcep30100101DTO;
import com.dcep.supergw.validation.Check301Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class Check301BizValidator implements ConstraintValidator<Check301Biz, Object> {

    private static final String MP01 = "MP01";
    private static final String MP02 = "MP02";
    private static final String MP03 = "MP03";
    private static final String COT03 = "COT03";
    private static final String TT16 = "TT16";

    private static final String TT23 = "TT23";

    private static final String SPM00 = "SPM00";
    private static final String SPM01 = "SPM01";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!(value instanceof Dcep30100101DTO)) {
            return true;
        }
        Dcep30100101DTO dto = (Dcep30100101DTO) value;
        if (dto.getCdtrInf() != null
            && (MP01.equals(dto.getCdtrInf().getMrchntPrprty()) || MP03.equals(dto.getCdtrInf().getMrchntPrprty()))
            && (dto.getMrchntTerInf() == null || StringUtils.isBlank(dto.getMrchntTerInf().getMrchntBizAddr()))) {
            CheckUtils.changeValidatorMsg("当商户属性为“MP01|MP03”时,商户经营地址必填", context);
            return false;
        }

        if (dto.getCdtrInf() != null
            && (MP02.equals(dto.getCdtrInf().getMrchntPrprty()) || MP03.equals(dto.getCdtrInf().getMrchntPrprty()))
            && (dto.getOrdrInf() == null || StringUtils.isBlank(dto.getOrdrInf().getPltfrmNm()))) {
            CheckUtils.changeValidatorMsg("当商户属性为“MP02|MP03”时,网络交易平台名称必填", context);
            return false;
        }

        if (COT03.equals(dto.getTrxInf().getCreOrdrTp()) && dto.getUserInf() == null) {
            CheckUtils.changeValidatorMsg("当下单类型为COT03时,用户信息必填", context);
            return false;
        }

        if (COT03.equals(dto.getTrxInf().getCreOrdrTp()) && StringUtils.isBlank(dto.getClbckUrl())) {
            CheckUtils.changeValidatorMsg("当下单类型为COT03时,回跳地址必填", context);
            return false;
        }

        if ((TT16.equals(dto.getTrxInf().getTrxTp())||COT03.equals(dto.getTrxInf().getCreOrdrTp())) && StringUtils.isBlank(dto.getTrxInf().getH5OrdrPgTp())) {
            CheckUtils.changeValidatorMsg("当交易类型为TT16或下单类型为COT03时,H5OrdrPgTp必填", context);
            return false;
        }

        //交易类型为TT23:企业订单支付，付款方必填
        if(TT23.equals(dto.getTrxInf().getTrxTp())){
            if(dto.getDbtrInf()==null){
                CheckUtils.changeValidatorMsg("当前交易类型为TT23,付款方信息必填", context);
                return false;
            }
        }
        if(dto.getDbtrInf()!=null){
            //主体签约+支付
            if(SPM00.equals(dto.getDbtrInf().getPtcMdTp())){
                if(dto.getDbtrInf().getDbtrCoyInf()==null){
                    CheckUtils.changeValidatorMsg("当签约模式为主体签约及支付,付款方企业信息必填", context);
                    return false;
                }
            }
            //钱包签约+支付
            if(SPM01.equals(dto.getDbtrInf().getPtcMdTp())){
                if(dto.getDbtrInf().getDbtrWltInf()==null){
                    CheckUtils.changeValidatorMsg("当签约模式为钱包签约及支付,付款方钱包信息必填", context);
                    return false;
                }
            }
        }

        return true;
    }
}
