package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc443.Dcep44300101DTO;
import com.dcep.supergw.validation.Check443Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : Check434BizValidator.java v 0.1 2019-09-09
 * @description :
 */
public class Check443BizValidator implements ConstraintValidator<Check443Biz, Object> {


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Dcep44300101DTO dto = (Dcep44300101DTO) value;


        //当管理类型为“MT03”，“MT05”和“MT06”时，OrgnlGrpHdr必填
        if(("MT03".equals(dto.getMgmtTp()) ||"MT06".equals(dto.getMgmtTp()) || "MT05".equals(dto.getMgmtTp())) && dto.getOrgnlGrpHdr()==null){
            CheckUtils.changeValidatorMsg("当管理类型为“MT03”，“MT05”和“MT06”时，OrgnlGrpHdr必填", context);
            return false;
        }

//签约行业务处理失败时，“业务回执状态”填写“PR01”，“业务拒绝码”根据实际情况填写，
        // “业务拒绝原因”填写详细的拒绝原因信息；“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、
        // “单位名称”、“单位证明文件类型”、“单位证明文件号码”、“法定代表人或单位负责人姓名”、
        // “法定代表人或单位负责人证件类型” 、“法定代表人或单位负责人证件号码” 、“单位联系手机号码”、
        // “单笔兑出业务金额上限”、“日累计业务兑出笔数上限”、“日累计兑出金额上限”、“年累计兑出业务笔数上限”、
        // “年累计兑出金额上限”等域不填写。
        if ("PR01".equals(dto.getRspsnInf().getRspsnSts())) {
            if(StringUtils.isAnyBlank(dto.getRspsnInf().getRjctCd(),dto.getRspsnInf().getRjctInf())) {
                CheckUtils.changeValidatorMsg("签约行业务处理失败，“业务回执状态”为“PR01”时，“业务拒绝码”、“业务拒绝原因”必填", context);
                return false;
            }
            if(!StringUtils.isAllBlank(dto.getPtcInf().getSgnAcctTp(),dto.getPtcInf().getSgnAcctId(),dto.getPtcInf().getSgnAcctNm(),dto.getPtcInf().getCorprtnNm(),
                    dto.getPtcInf().getCorprtnIDTp(),dto.getPtcInf().getCorprtnIDNo(),dto.getPtcInf().getLglRepNm(),dto.getPtcInf().getLglRepIDTp(),dto.getPtcInf().getLglRepIDNo(),
                    dto.getPtcInf().getTel(),dto.getPtcInf().getDlTtlCnt(),dto.getPtcInf().getAnlTtlCnt())
                    || (null != dto.getPtcInf().getSnglTxAmtLmt() || (null != dto.getPtcInf().getDlTtlAmtLmt()) || (null != dto.getPtcInf().getAnlTtlAmtLmt()) )) {
                CheckUtils.changeValidatorMsg("签约行业务处理失败，“业务回执状态”为“PR01”时，“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、" +
                        "        “单位名称”、“单位证明文件类型”、“单位证明文件号码”、“法定代表人或单位负责人姓名”、" +
                        "        “法定代表人或单位负责人证件类型” 、“法定代表人或单位负责人证件号码” 、“单位联系手机号码”、" +
                        "        “单笔兑出业务金额上限”、“日累计业务兑出笔数上限”、“日累计兑出金额上限”、“年累计兑出业务笔数上限”、" +
                        "        “年累计兑出金额上限”等域不填写", context);
                return false;
            }
        }
//账户开户行业务处理成功，且签约成功时：
//“业务回执状态”填写“PR00”，“业务拒绝码”、“业务拒绝原因”等域不填写；
//“签约协议号”、“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、“单位名称”、“单位证明文件类型”、“单位证明文件号码”、
// “法定代表人或单位负责人姓名”、“法定代表人或单位负责人证件类型” 、“法定代表人或单位负责人证件号码” 、“单位联系手机号码”、“协议生效日期”等域必须填写；
//“单笔兑出业务金额上限”、“日累计业务兑出笔数上限”、“日累计兑出金额上限”、“年累计兑出业务笔数上限”、“年累计兑出金额上限”等域则根据协议内容选择性填写。
        if (("MT05".equals(dto.getMgmtTp())
                || "MT06".equals(dto.getMgmtTp())) && "PR00".equals(dto.getRspsnInf().getRspsnSts())) {
            if(!StringUtils.isAllBlank(dto.getRspsnInf().getRjctCd(),dto.getRspsnInf().getRjctInf())) {
                CheckUtils.changeValidatorMsg("账户开户行业务处理成功，且签约成功，“业务回执状态”为“PR00”时，“业务拒绝码”、“业务拒绝原因”等域不填写", context);
                return false;
            }
            if(StringUtils.isAnyBlank(dto.getPtcInf().getPtcId(),dto.getPtcInf().getSgnAcctTp(),dto.getPtcInf().getSgnAcctId(),dto.getPtcInf().getSgnAcctNm(),dto.getPtcInf().getCorprtnNm(),
                    dto.getPtcInf().getCorprtnIDTp(),dto.getPtcInf().getCorprtnIDNo(),dto.getPtcInf().getLglRepNm(),dto.getPtcInf().getLglRepIDTp(),dto.getPtcInf().getLglRepIDNo(),
                    dto.getPtcInf().getTel(),dto.getPtcInf().getPtcFctvDt())) {
                CheckUtils.changeValidatorMsg("账户开户行业务处理成功，且签约成功，“业务回执状态”为“PR00”时，“签约协议号”、“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、“单位名称”、“单位证明文件类型”、“单位证明文件号码”、" +
                        "“法定代表人或单位负责人姓名”、“法定代表人或单位负责人证件类型” 、“法定代表人或单位负责人证件号码” 、“单位联系手机号码”、“协议生效日期”等域必须填写", context);
                return false;
            }
        }
//账户开户行业务处理成功，且解约成功时：
//“业务回执状态”填写“PR00”，“业务拒绝码”、“业务拒绝原因”等域不填写；
//“签约协议号”、“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、“单位名称”、“单位证明文件类型”、“单位证明文件号码”、
// “法定代表人或单位负责人姓名”、“法定代表人或单位负责人证件类型” 、“法定代表人或单位负责人证件号码” 、“单位联系手机号码”、等域必须填写。

        if (("MT03".equals(dto.getMgmtTp()) || "MT04".equals(dto.getMgmtTp()))
                && "PR00".equals(dto.getRspsnInf().getRspsnSts())) {
            if(!StringUtils.isAllBlank(dto.getRspsnInf().getRjctCd(),dto.getRspsnInf().getRjctInf())) {
                CheckUtils.changeValidatorMsg("账户开户行业务处理成功，且解约成功，“业务回执状态”为“PR00”时，“业务拒绝码”、“业务拒绝原因”等域不填写", context);
                return false;
            }
            if(StringUtils.isAnyBlank(dto.getPtcInf().getPtcId(),dto.getPtcInf().getSgnAcctTp(),dto.getPtcInf().getSgnAcctId(),dto.getPtcInf().getSgnAcctNm(),dto.getPtcInf().getCorprtnNm(),
                    dto.getPtcInf().getCorprtnIDTp(),dto.getPtcInf().getCorprtnIDNo(),dto.getPtcInf().getLglRepNm(),dto.getPtcInf().getLglRepIDTp(),dto.getPtcInf().getLglRepIDNo(),
                    dto.getPtcInf().getTel())) {
                CheckUtils.changeValidatorMsg("账户开户行业务处理成功，且解约成功，“业务回执状态”为“PR00”时，“签约协议号”、“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、“单位名称”、“单位证明文件类型”、“单位证明文件号码”、" +
                        "“法定代表人或单位负责人姓名”、“法定代表人或单位负责人证件类型” 、“法定代表人或单位负责人证件号码” 、“单位联系手机号码”、等域必须填写", context);
                return false;
            }
        }

        return true;

    }
}
