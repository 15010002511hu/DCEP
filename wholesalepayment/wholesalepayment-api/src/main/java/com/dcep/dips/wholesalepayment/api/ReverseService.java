/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustRespDTO;
import com.dubbo.ldc.ZoneRouter;

/**
 * 业务撤销服务
 * @author hx.zhaolei
 */
@ZoneRouter
public interface ReverseService {
    /**
     * 用于机构对结算排队业务发起的业务撤销处理
     * @return
     * @throws DcepException
     */
    Response<EnvelopeDTO> process(EnvelopeDTO<GwDTO> envelopeDTO) throws DcepException;

}
