/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustRespDTO;
import com.dubbo.ldc.ZoneRouter;

/**
 * @author hx.zhaolei
 */
@ZoneRouter
public interface ChainService {
    /**
     * 链上交易请求
     * @return
     * @throws DcepException
     */
    Response<OnChainAdjustRespDTO> onChainAdjust(OnChainAdjustReqDTO onChainAdjustReqDTO) throws DcepException;

}
