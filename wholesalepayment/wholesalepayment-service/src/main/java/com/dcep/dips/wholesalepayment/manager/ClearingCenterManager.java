package com.dcep.dips.wholesalepayment.manager;

import com.dcep.clearingcenter.dto.settlement.HvpsReqDTO;
import com.dcep.clearingcenter.dto.settlement.HvpsRspDTO;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;

/**
 * 准备金系统管理接口
 */
public interface ClearingCenterManager {
    void delStorageForwardAndInsertHvpsTrans(String msgId, String msgTp, HvpsReqDTO hvpsReqDTO, HvpsRspDTO hvpsRspDTO);

    void asyncSendHvps(EnvelopeDTO<GwDTO> gwReqDTO);

    void asyncSendHvps(ZeroOutReqDTO zeroOutReqDTO, FundAdjustProdDO fundAdjustProdDO);
}
