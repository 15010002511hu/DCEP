package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.StorageForwardDO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;

/**
 * 存储转发管理接口
 */
public interface StorageForwardManager {
    void saveForActgAdjustDecrease(FundingDTO fundingDTO);
    void saveForHvpsAdjustDecrease(FundingDTO fundingDTO);
    void saveForHvpsZeroOut(String msgId, String msgTp, ZeroOutReqDTO zeroOutReqDTO);
    void saveForInst(AccountingInstrDO accountingInstrDO, boolean isPassDebitConfirm);
    void saveForInst(AccountingInstrDO accountingInstrDO, String receiver);
    StorageForwardDO saveForMbridge(GenericEnvelopeDTO<GenericGwDTO> genericReq, String msgId, String msgTp, String reciver, String lockFlag);
    StorageForwardDO saveForInst(EnvelopeDTO<GwDTO> req, String reciver);
    StorageForwardDO saveForInst(String msgId,String msgTp, String reciver,EnvelopeDTO<GwDTO> req);
    void unLock(boolean status, StorageForwardDO storageForwardDO);
}
