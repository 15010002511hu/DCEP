package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.common.dto.dc411.Dcep41100101DTO;
import com.dcep.dips.common.dto.dc417.Dcep41700101DTO;
import com.dcep.dips.common.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.api.QueryService;
import com.dcep.dips.wholesalepayment.common.utils.SoapHeaderUtil;
import com.dcep.dips.wholesalepayment.manager.ClearingDtlQueryManager;
import com.dcep.dips.wholesalepayment.manager.ClearingStsQueryManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@Slf4j
@DubboService
public class QueryServiceImpl implements QueryService {

	@Resource
	ClearingStsQueryManager clearingStsQueryManager;
	@Resource
	ClearingDtlQueryManager clearingDtlQueryManager;

	@Override
	public Response<EnvelopeDTO<GwDTO>> querySts(EnvelopeDTO<GwDTO> in) {
		log.info("QueryService.querySts start: MsgSN={}", in.getSoapHeader().getMsgSN());
		GwDTO inGwDTO = in.body();
		// 从网关收411返回412
		return new Response<EnvelopeDTO<GwDTO>>(new EnvelopeDTO<GwDTO>(
				SoapHeaderUtil.createSoapHeader(MsgTpEnum.TXN_STATE_RESPONSE.getCode(),
						in.getSoapHeader().getMsgSN(), InfoCacheUtil.getPbocInf(), in.getSoapHeader().getSender()),
				clearingStsQueryManager.queryClearingStsFromOrg((Dcep41100101DTO) inGwDTO)));
	}

	@Override
	public Response<EnvelopeDTO<GwDTO>> queryDtl(EnvelopeDTO<GwDTO> in) {
		log.info("QueryService.queryDtl start: MsgSN={}", in.getSoapHeader().getMsgSN());
		GwDTO inGwDTO = in.body();
		// 从网关收417返回418
		return new Response<EnvelopeDTO<GwDTO>>(new EnvelopeDTO<GwDTO>(
				SoapHeaderUtil.createSoapHeader(MsgTpEnum.TXN_DETAIL_RESPONSE.getCode(),
						in.getSoapHeader().getMsgSN(), InfoCacheUtil.getPbocInf(), in.getSoapHeader().getSender()),
				clearingDtlQueryManager.queryClearingDtlFromOrg((Dcep41700101DTO) inGwDTO)));
	}
}
