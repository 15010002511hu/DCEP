package com.dcep.dips.wholesalepayment.manager.redo;

import com.dcep.dips.wholesalepayment.dal.mapper.CommonRecordMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 结算类产品。SettlementProdDO
 */
@Slf4j
public abstract class SettleRedoManager implements RedoManager {

    @Resource
    protected SettlementProdMapper settlementProdMapper;
    @Resource
    protected CommonRecordMapper commonRecordMapper;

    @Override
    public boolean redo(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO){
        if(stsctrlDO==null || accountingInstrDO==null){
            log.error("SettleRedoManager.redo stsctrlDO or accountingInstrDO is null");
            return false;
        }
        SettlementProdDO settle = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(stsctrlDO.getMsgId()));
        if(settle==null ){
            log.error("SettleRedoManager.redo Error MsgId:{}, settle is null", stsctrlDO.getMsgId());
            return false;
        }
        if(settle.finished()){
            log.error("SettleRedoManager.redo Error MsgId:{}, settle is finished,bizSts:{}", stsctrlDO.getMsgId(),settle.getBizSts());
            return true;
        }

        //超时处理,将交易置为失败，并发送失败报文
        if(new Date().compareTo(stsctrlDO.getConfirmTimeout()) > 0){
            return presume(stsctrlDO,accountingInstrDO,settle);
        }
        //补偿处理
        return process( stsctrlDO,  accountingInstrDO, settle);
    }

    /**
     * 超时后的推定处理
     * @param stsctrlDO
     * @param accountingInstrDO
     * @param settle
     * @return
     */
    protected  abstract boolean presume(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO,SettlementProdDO settle);

    /**
     * 重试处理
     * @param stsctrlDO
     * @param accountingInstrDO
     * @param settle
     * @return
     */
    protected abstract boolean  process(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO,SettlementProdDO settle);


}
