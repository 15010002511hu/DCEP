package com.dcep.supergw.validation.validator;

import com.dcep.common.utils.CheckUtils;
import com.dcep.supergw.dto.dc420.Dcep42000101DTO;
import com.dcep.supergw.validation.Check420Biz;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : duzhong
 * @version : Check416BizValidator.java v 0.1 2020-05-11
 * @description :
 */
public class Check420BizValidator implements ConstraintValidator<Check420Biz, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Dcep42000101DTO dto420 = (Dcep42000101DTO) value;

		// 当查询处理状态为PR00时填写
		if ("PR00".equalsIgnoreCase(dto420.getBizQryRef().getQryRs())) {
			if (null == dto420.getBizRpt()) {
				CheckUtils.changeValidatorMsg("查询处理状态为PR00时需要填写应答的原业务信息", context);
				return false;
			}
			if ("PR01".equalsIgnoreCase(dto420.getBizRpt().getTrnRs())) {
				if (StringUtils.isBlank(dto420.getBizRpt().getRsn().getRjctCd())
						|| StringUtils.isBlank(dto420.getBizRpt().getRsn().getRjctInf())) {
					CheckUtils.changeValidatorMsg("当查询处理状态为PR01时需要填写应答拒绝信息", context);
					return false;
				}
			}
			if ("PR00".equalsIgnoreCase(dto420.getBizRpt().getTrnRs())
					&& "MT03".equalsIgnoreCase(dto420.getBizRpt().getOrgnlTxInf().getMgmtTp())) {
				if ((null != dto420.getBizRpt().getRsn())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getPtcId())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getSgnAcctPtyId())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getSgnAcctTp())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getSgnAcctId())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getSgnAcctNm())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getCorprtnNm())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getCorprtnIDTp())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getCorprtnIDNo())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getLglRepNm())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getLglRepIDTp())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getLglRepIDNo())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getTel())
						|| (null != dto420.getBizRpt().getOrgnlTxInf().getSnglTxAmtLmt())
						|| StringUtils.isNoneBlank(dto420.getBizRpt().getOrgnlTxInf().getDlTtlCnt())
						|| (null != dto420.getBizRpt().getOrgnlTxInf().getDlTtlAmtLmt())
						|| StringUtils.isNoneBlank(dto420.getBizRpt().getOrgnlTxInf().getAnlTtlCnt())
						|| (null != dto420.getBizRpt().getOrgnlTxInf().getAnlTtlAmtLmt())) {
					CheckUtils.changeValidatorMsg(
							"原交易状态为“PR00”，且管理类型为“MT03-解约申请”时，“业务拒绝码”、“业务拒绝原因”等域不填写；“签约协议号”、“单位银行账户所属运营机构编码”、“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、“单位名称”、“单位证明文件类型”、“单位证明文件号码”、“法定代表人或单位负责人姓名”、“法定代表人或单位负责人证件类型”、“法定代表人或单位负责人证件号码”、“单位联系手机号码”必须填写正确的业务信息；“单笔兑出业务金额上限”、“日累计业务兑出笔数上限”、“日累计兑出金额上限”、“年累计兑出业务笔数上限”、“年累计兑出金额上限”等域不填写",
							context);
					return false;

				}
			}

			if ("PR00".equalsIgnoreCase(dto420.getBizRpt().getTrnRs())
					&& ("MT05".equalsIgnoreCase(dto420.getBizRpt().getOrgnlTxInf().getMgmtTp())
							|| "MT06".equalsIgnoreCase(dto420.getBizRpt().getOrgnlTxInf().getMgmtTp()))) {
				if ((null != dto420.getBizRpt().getRsn())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getPtcId())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getSgnAcctPtyId())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getSgnAcctTp())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getSgnAcctId())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getSgnAcctNm())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getCorprtnNm())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getCorprtnIDTp())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getCorprtnIDNo())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getLglRepNm())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getLglRepIDTp())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getLglRepIDNo())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getTel())
						|| StringUtils.isBlank(dto420.getBizRpt().getOrgnlTxInf().getPtcFctvDt())) {
					CheckUtils.changeValidatorMsg(
							"原交易状态为“PR00”，且管理类型为“MT05-网关签约”或“MT06-普通签约”时，“业务拒绝码”、“业务拒绝原因”等域不填写；“签约协议号”、“单位银行账户所属运营机构编码”、“单位银行账户类型”、“单位银行账户账号”、“单位银行账户户名”、“单位名称”、“单位证明文件类型”、“单位证明文件号码”、“法定代表人或单位负责人姓名”、“法定代表人或单位负责人证件类型”、“法定代表人或单位负责人证件号码”、“单位联系手机号码”、“协议生效日期”等域必须填写",
							context);
					return false;

				}
			}
		}
		// 当查询处理状态为PR01时填写
		if ("PR01".equalsIgnoreCase(dto420.getBizQryRef().getQryRs())) {
			if (null == dto420.getOprlErr()) {
				CheckUtils.changeValidatorMsg("当原交易状态为“PR01”时必填应答拒绝信息", context);
				return false;
			}
		}

		return true;

	}

}
