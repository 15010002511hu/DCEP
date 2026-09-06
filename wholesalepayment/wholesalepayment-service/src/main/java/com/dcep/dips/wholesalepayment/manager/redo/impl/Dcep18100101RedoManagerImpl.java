package com.dcep.dips.wholesalepayment.manager.redo.impl;

import com.alibaba.fastjson.JSON;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.constants.Constant;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustRespDTO;
import com.dcep.dips.wholesalepayment.dal.bo.ActgAdjustRespBO;
import com.dcep.dips.wholesalepayment.dal.mapper.CommonRecordMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonRecordDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.FundingManager;
import com.dcep.dips.wholesalepayment.manager.redo.FundingRedoManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 资金调拨类
 */
@Slf4j
@Service
public class Dcep18100101RedoManagerImpl extends FundingRedoManager {

    @Resource
    private AccountingManager accountingManager;
    @Resource
    private FundingManager fundingManager;
    @Resource
    private CommonRecordMapper commonRecordMapper;

    @PostConstruct
    public void register() {
        rodo.put(MsgTpEnum.CDT_FUND_DECREASE.getCode(), this);
        rodo.put(MsgTpEnum.CDT_PRE_FUND_DECREASE.getCode(), this);
    }


    @Override
    protected boolean process(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO,FundAdjustProdDO fundAdjustProdDO){
        //TODO liuqingliang
        if(ClearingStatusEnum.PROCESS.getCode().equals(fundAdjustProdDO.getBizSts())){
            Response<AdjustRespDTO> actgResp = accountingManager.adjust(accountingInstrDO, null); // 注资调减、预注资调减
            if (!actgResp.isSuccess()) {
                // 结算钱包：通讯异常（状态不明）
                // 抛异常返回机构911报文，等待存储转发重发，或者机构重发
                log.error("结算钱包：通讯异常（状态不明）");
                return false;
            }

            ActgAdjustRespBO actgAdjustRespBO = new ActgAdjustRespBO(actgResp.getResult());

            CommonRecordDO commonRecordDO = commonRecordMapper.selectByPrimaryKey(new CommonRecordDO(fundAdjustProdDO.getMsgId(), fundAdjustProdDO.getMsgTp()));
            FundingDTO gwReqDTO = JSON.parseObject(commonRecordDO.getDocument(), FundingDTO.class);
            fundingManager.updateDecreasePrepareStatus(gwReqDTO, accountingInstrDO, actgAdjustRespBO);

            if (Constant.AccountingStatus.SUCCESS.equals(actgAdjustRespBO.getAccountingStatus())) {
                //TODO 待实现接口
//                clearingCenterManager.asyncSendHvps(gwReqDTO);
            }
        }else if(ClearingStatusEnum.ACCEPTED.getCode().equals(fundAdjustProdDO.getBizSts())){
            //已插入存储转发表，无需补偿处理
        }
        return false;
    }
}
