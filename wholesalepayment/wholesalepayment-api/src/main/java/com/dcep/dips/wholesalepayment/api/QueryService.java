package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dubbo.ldc.ZoneRouter;

/**
 * 交易查询服务处理类，用于中央处理模式查询业务报文
 * 报文业务：交易状态查询411/412、交易明细查询417/418报文
 *
 */
@ZoneRouter
public interface QueryService {

	Response<EnvelopeDTO<GwDTO>> querySts(EnvelopeDTO<GwDTO> in)
			throws DcepException;
	
	Response<EnvelopeDTO<GwDTO>> queryDtl(EnvelopeDTO<GwDTO> in)
			throws DcepException;

}
