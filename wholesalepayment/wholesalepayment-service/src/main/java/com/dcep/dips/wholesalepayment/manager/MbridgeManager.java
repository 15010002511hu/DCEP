package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dal.bo.OnChainTransInfoBO;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.StorageForwardDO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;

public interface MbridgeManager {

	/**
	 * 预处理
	 * @param gwReqDTO
	 * @param genericGwReqDTO
	 * @return
	 */
	AccountingInstrDO prepare(EnvelopeDTO<GwDTO> gwReqDTO, GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO);

	/**
	 * 更新终态
	 * @param genericReq    		用于存储转发
	 * @param req			 		用于存储转发
	 * @param accountingInstrDO
	 * @param clearingStatusEnum
	 * @param updateAcctFlag		更新记账指令表标志
	 * @param saveDcep200Flag		发送dcep200标志
	 * @param saveDcepFlag			发送机构标志
	 * @param saveMcbsFlag			发送货币桥标志
	 * @param reciver				接收方
	 * @param mcbsreciver			接收方
	 */
	void pendingFinish(GenericEnvelopeDTO<GenericGwDTO> genericReq, EnvelopeDTO<GwDTO> req, AccountingInstrDO accountingInstrDO, ClearingStatusEnum clearingStatusEnum,
					       boolean updateAcctFlag, boolean saveDcep200Flag, boolean saveDcepFlag, boolean saveMcbsFlag, String reciver, String mcbsreciver);

	/**
	 * 同步记账交易登记
	 * @param onChainTransInfo
	 * @param commonRecordFlag
	 * @param object
	 * @return
	 */
	Response<AccountingInstrDO> onChainRecord(OnChainTransInfoBO onChainTransInfo, boolean commonRecordFlag, Object object);

	/**
	 * 更新表状态并发报
	 * @param mBridgeReqEnvelopeDTO
	 * @param req
	 * @param accountingInstrDO
	 * @param storageForwardDO
	 * @param orgActgSts
	 * @param orgBizSts
	 */
	void updateAndSend(GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO, EnvelopeDTO<GwDTO> req, AccountingInstrDO accountingInstrDO,
					   StorageForwardDO storageForwardDO, String orgActgSts, String orgBizSts);

	/**
	 * 调用货币桥网关
	 *
	 * @param genericReq
	 * @return
	 */
	boolean hlht2MbridgeGateway(GenericEnvelopeDTO<GenericGwDTO> genericReq);

	/**
	 * 异步处理
	 * @param genericReq
	 * @param req
	 * @param accountingInstrDO
	 * @param mcbsMsgId
	 */
	void hlht2MbridgeAsync(GenericEnvelopeDTO<GenericGwDTO> genericReq, EnvelopeDTO<GwDTO> req, AccountingInstrDO accountingInstrDO, String mcbsMsgId);

	/**
	 * 异步处理
	 * @param genericReq
	 * @param req
	 * @param accountingInstrDO
	 */
	void mBridge2HlhtAsync(GenericEnvelopeDTO<GenericGwDTO> genericReq, EnvelopeDTO<GwDTO> req, AccountingInstrDO accountingInstrDO);

	/**
	 * 调用结算钱包transfer接口
	 * @param accountingInstrDO
	 */
	void transfer(AccountingInstrDO accountingInstrDO, boolean queueFlag);

}
