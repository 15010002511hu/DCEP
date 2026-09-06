package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dal.bo.ActgAdjustRespBO;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dal.model.HvpsTransDO;
import com.dcep.dips.wholesalepayment.dto.BizStatusDTO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.funding.IncreaseReqDTO;
import com.dcep.dips.wholesalepayment.dto.hvps.ClearReportReqDTO;
import com.dcep.dips.wholesalepayment.enums.HvpsAdjStatusEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;

import java.util.List;

/**
 * 注资预注资-资金调整管理接口
 */
public interface FundingManager {
    Response<AccountingInstrDO> decreaseRecord(EnvelopeDTO<GwDTO> gwReqDTO);

    Response<FundAdjustProdDO> zeroOutRecord(ZeroOutReqDTO zeroOutReqDTO);

    Response<AccountingInstrDO> increaseRecord(IncreaseReqDTO increaseReqDTO);

    Response<BizStatusDTO> checkHvpsBusinessInfo(IncreaseReqDTO incrReqDTO);

    FundAdjustProdDO updateDecreasePrepareStatus(FundingDTO fundingDTO,
                                                 AccountingInstrDO accountingInstrDO, ActgAdjustRespBO actgAdjustRespBO);

    void updatePrepareStatus(FundAdjustProdDO orgnlFundAdjustProdDO, FundAdjustProdDO fundAdjustProdDO,
                             String transId, ActgAdjustRespBO actgAdjustRespBO);

    /**
     * 注资调减、预注资调减、清零，结算钱包返回后，更新finish状态
     * @param reportReqDTO 大额通知DTO
     * @param orgnlHvpsTransDO 大额对接产品表原DO
     * @param orgnlFundAdjustProdDO 资金调整产品表原DO
     * @param finishAccInstrDO 记账指令表DO
     * @param actgAdjustRespBO 结算钱包返回应答DTO
     * @param hvpsAdjStatus 大额资金调整结果
     */
    List<EnvelopeDTO<GwDTO>> updateDecreaseFinishStatus(ClearReportReqDTO reportReqDTO,
                                                        HvpsTransDO orgnlHvpsTransDO, FundAdjustProdDO orgnlFundAdjustProdDO,
                                                        AccountingInstrDO finishAccInstrDO, ActgAdjustRespBO actgAdjustRespBO,
                                                        HvpsAdjStatusEnum hvpsAdjStatus);

    /**
     * 注资调增，结算钱包返回后，更新finish状态
     * @param increaseReqDTO 大额请求对DTO
     * @param accountingInstrDO 记账指令DO
     * @param actgAdjustRespBO 结算钱包返回应答
     */
    List<EnvelopeDTO<GwDTO>> updateIncreaseFinishStatus(IncreaseReqDTO increaseReqDTO,
                                    AccountingInstrDO accountingInstrDO, ActgAdjustRespBO actgAdjustRespBO);

    /**
     * 预注资调增，结算钱包返回后，更新finish状态
     * @param reportReqDTO 大额通知DTO
     * @param orgnlHvpsTransDO 原大额对接DO
     * @param orgnlFundAdjustProdDO 原资金调整DO
     * @param accountingInstrDO 结算钱包记账指令DO
     * @param actgAdjustRespBO 结算钱包返回BO
     */
    List<EnvelopeDTO<GwDTO>> updatePreIncreaseFinishStatus(ClearReportReqDTO reportReqDTO,
                                                     HvpsTransDO orgnlHvpsTransDO, FundAdjustProdDO orgnlFundAdjustProdDO,
                                                     AccountingInstrDO accountingInstrDO, ActgAdjustRespBO actgAdjustRespBO);

    void updateFinishStatus(HvpsTransDO orgnlHvpsTransDO, HvpsTransDO hvpsTransDO,
                            FundAdjustProdDO orgnlFundAdjustProdDO, FundAdjustProdDO fundAdjustProdDO,
                            String transId, ActgAdjustRespBO actgAdjustRespBO);

    /**
     * 异步发送机构通知报文
     * @param envelopeDTO 机构报文
     * @param msgId 原业务报文标识号
     */
    void asyncSendInst(List<EnvelopeDTO<GwDTO>> dtoList, String msgId, String envInfo);

    /**
     * 发送机构通知报文，并删除存储转发
     * @param envelopeDTO 机构报文
     * @param msgId 原业务报文标识号
     */
    void sendInstAndDelStorageForward(List<EnvelopeDTO<GwDTO>> dtoList, String msgId, String envInfo);

    /**
     * 注资调减、预注资调减、清零
     * @param reportReqDTO 大额请求报文
     * @param orgnlHvpsTransDO 报文标识号
     * @param orgnlFundAdjustProdDO 运营中心的清算状态
     */
    Response<BizStatusDTO> decreaseFinish(ClearReportReqDTO reportReqDTO, HvpsTransDO orgnlHvpsTransDO,
                                          FundAdjustProdDO orgnlFundAdjustProdDO, HvpsAdjStatusEnum hvpsAdjStatus);

    /**
     * 预注资调增
     * @param clearReportReqDTO 大额通知报文
     * @param orgnlHvpsTransDO 原大额对接产品表DO
     * @param orgnlFundAdjustProdDO 原资金调整产品表DO
     */
    Response<BizStatusDTO> preIncreaseFinish(ClearReportReqDTO clearReportReqDTO, HvpsTransDO orgnlHvpsTransDO, FundAdjustProdDO orgnlFundAdjustProdDO);
}
