package com.dcep.dips.wholesalepayment.manager.redo.impl;

import com.dcep.dips.wholesalepayment.dal.mapper.CommonRecordMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.enums.ActgStsEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.ChainManager;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.SettlementManager;
import com.dcep.dips.wholesalepayment.manager.redo.SettleRedoManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 区块链交易重试
 */
@Slf4j
@Service
public class ChainRedoManagerImpl extends SettleRedoManager {

    @Resource
    private CommonManager commonManager;
    @Resource
    private SettlementManager settlementManager;

    @PostConstruct
    public void register() {
        rodo.put(MsgTpEnum.CHAIN_REQUEST_UP.getCode(), this);
        rodo.put(MsgTpEnum.CHAIN_REQUEST_DOWN.getCode(), this);
    }


    @Override
    protected boolean presume(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO, SettlementProdDO settle) {
        //只能不断查询，并处理结果
        AccountingInstrDO queryResult = commonManager.queryTransferStatus(accountingInstrDO);
        if(queryResult == null){
            accountingInstrDO.setActgSts(ActgStsEnum.FAILED.getCode());
            accountingInstrDO.setActgPrcCd(WholesaleErrorEnum.BUSI_COMP_ERROR.getCode());
            accountingInstrDO.setActgPrcInf(WholesaleErrorEnum.BUSI_COMP_ERROR.getDescription());
            return false;
        }
        settlementManager.settleFinshed(accountingInstrDO);

        //notify()
        //返回false即可，因为如果是终态，settleFinshed会删除超时控制
        return false;
    }

    @Override
    protected boolean process(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO,SettlementProdDO settle) {
//        if(ClearingStatusEnum.WAIT_SETTLE.getCode().equals(settle.getBizSts())){
//            CommonRecordDO commonRecordDO = commonRecordMapper.selectByPrimaryKey(new CommonRecordDO(settle.getMsgId(), settle.getMsgTp()));
//            if(commonRecordDO==null){
//                return false;
//            }
//            OnChainAdjustReqDTO req = JSON.parseObject(commonRecordDO.getDocument(), OnChainAdjustReqDTO.class);
//            chainManager.transfer(accountingInstrDO,settle.getSttlmDt(),req.getUseCurrentSystemFlag());
//        }
//        return false;
        //只能不断查询，并处理结果
        AccountingInstrDO queryResult = commonManager.queryTransferStatus(accountingInstrDO);
        if(queryResult == null){
            return false;
        }
        settlementManager.settleFinshed(accountingInstrDO);

        //返回false即可，因为如果是终态，settleFinshed会删除超时控制
        return false;
    }

}
