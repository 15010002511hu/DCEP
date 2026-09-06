/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.api.WholesaleReportService;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dto.acctrans.AccountingReportDTO;
import com.dcep.dips.wholesalepayment.enums.ActgStsEnum;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.manager.WholesaleReportManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@Slf4j
@DubboService
public class WholesaleReportServiceImpl implements WholesaleReportService {
    @Resource
    AccountingInstrMapper accountingInstrMapper;
    @Resource
    WholesaleReportManager wlolesaleReportManager;

    @Override
    public Response<String> report(AccountingReportDTO accountingReportDTO) throws DcepException {
        // 1.通过原transId查询记账指令表
        AccountingInstrDO orgAccountingInstrDO = accountingInstrMapper.selectByPrimaryKey(new AccountingInstrDO(accountingReportDTO.getTransId()));
        if (orgAccountingInstrDO == null) {
            log.error("report orgAccountingInstrDO not exist transId:{}", accountingReportDTO.getTransId());
            return new Response<>(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        // 2.判断当前状态如果已为终态，直接返回(成功、失败、排队取消、排队退回)
        if (ActgStsEnum.SUCCESS.getCode().equals(orgAccountingInstrDO.getActgSts()) || ActgStsEnum.FAILED.getCode().equals(orgAccountingInstrDO.getActgSts())
                || ActgStsEnum.QUEUE_CANCELED.getCode().equals(orgAccountingInstrDO.getActgSts()) || ActgStsEnum.QUEUE_RETURNED.getCode().equals(orgAccountingInstrDO.getActgSts())) {
            return new Response<>(true, "success");
        }
        // 3.根据通知结算状态进行处理
        if (MsgTpEnum.CDT_REQUEST_ASYN.getCode().equals(orgAccountingInstrDO.getMsgTp())){
            // 原交易为203
            wlolesaleReportManager.processForMbridge(accountingReportDTO, orgAccountingInstrDO);
        } else {
            wlolesaleReportManager.process(accountingReportDTO, orgAccountingInstrDO);
        }
        return null;
    }
}
