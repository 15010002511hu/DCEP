package com.dcep.dips.wholesalepayment.manager.redo;

import com.dcep.dips.wholesalepayment.dal.mapper.FundAdjustProdMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 资金调整类产品 FundAdjustProdDO
 */
public abstract class FundingRedoManager implements RedoManager {

    @Resource
    protected FundAdjustProdMapper fundAdjustProdMapper;


    @Override
    public boolean redo(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO){
        if(stsctrlDO==null || accountingInstrDO==null){
            return false;
        }
        FundAdjustProdDO fundAdjustProdDO = fundAdjustProdMapper.selectByPrimaryKey(new FundAdjustProdDO(stsctrlDO.getMsgId()));
        if(fundAdjustProdDO==null ) {
            return false;
        }
        //超时处理,将交易置为失败，并发送失败报文
        if(new Date().compareTo(stsctrlDO.getConfirmTimeout()) > 0){
            //TODO 搞成失败,双边发送200报文
            return false;
        }



        return process( stsctrlDO,  accountingInstrDO, fundAdjustProdDO);
    }

    protected  boolean presume(){return false;};

    protected abstract boolean  process(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO,FundAdjustProdDO fundAdjustProdDO);



}
