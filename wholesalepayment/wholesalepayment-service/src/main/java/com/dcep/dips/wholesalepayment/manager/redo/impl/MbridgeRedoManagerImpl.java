package com.dcep.dips.wholesalepayment.manager.redo.impl;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.common.enums.ClearingProdErrorEnum;
import com.dcep.dips.wholesalepayment.api.DcepSndMbridgeService;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.ChainTransMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.CommonRecordMapper;
import com.dcep.dips.wholesalepayment.dal.model.*;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.dc203.Dcep20301001DTO;
import com.dcep.dips.wholesalepayment.dto.dc213.Dcep21301001DTO;
import com.dcep.dips.wholesalepayment.enums.ActgStsEnum;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.DcepToMbridgeConvertManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeToDcepConvertManager;
import com.dcep.dips.wholesalepayment.manager.redo.SettleRedoManager;
import com.dcep.gateway.mcbdc.api.GwoutService;
import com.dcep.gateway.mcbdc.dto.mcbs200.Mcbs20000101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs201.Mcbs20100101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs202.Mcbs20200101DTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dcep.gateway.mcbdc.dto.soap.McbsEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.McbsGwDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 货币桥业务
 */
@Slf4j
@Service
public class MbridgeRedoManagerImpl extends SettleRedoManager {
    @Resource
    private MbridgeManager mbridgeManager;
    @Resource
    private DcepToMbridgeConvertManager dcep2mBridge;
    @Resource
    private CommonRecordMapper commonRecordMapper;
    @Resource
    private CommonManager commonManager;
    @Resource
    private ChainTransMapper chainTransMapper;
    @DubboReference
    private GwoutService mBridgeGwoutService;
    @Resource
    private DcepSndMbridgeService dcepSndMbridgeService;
    @Resource
    private MbridgeToDcepConvertManager mbridgeToDcepConvertManager;
    @PostConstruct
    public void register() {
        rodo.put(MsgTpEnum.CDT_REQUEST_ASYN.getCode(), this);
        rodo.put(MsgTpEnum.DBT_REQUEST_ASYN.getCode(), this);
    }

    private static final String KEY_ENVELOPE_DTO = "envelopeDTO";
    private static final String KEY_GENERIC_ENVELOPE_DTO = "genericEnvelopeDTO";
    private static final Map<String, TypeReference> map = new HashMap<>();
    static {
        map.put(MsgTpEnum.CDT_REQUEST_ASYN.getCode(), new TypeReference<EnvelopeDTO<Dcep20301001DTO>>() {});//返回203
        map.put(MsgTpEnum.DBT_REQUEST_ASYN.getCode(), new TypeReference<EnvelopeDTO<Dcep21301001DTO>>() {});//返回213
        map.put(MsgTpEnum.CDT_REQUEST_MCBS.getCode(), new TypeReference<McbsEnvelopeDTO<Mcbs20000101DTO>>() {});//返回mcbs200
        map.put(MsgTpEnum.DBT_REQUEST_MCBS.getCode(), new TypeReference<McbsEnvelopeDTO<Mcbs20100101DTO>>() {});//返回mcbs201
        map.put(MsgTpEnum.PAY_REFUND_MCBS.getCode(), new TypeReference<McbsEnvelopeDTO<Mcbs20200101DTO>>() {});//返回mcbs202
    }


    @Override
    protected boolean presume(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO, SettlementProdDO settle) {
        // 交易状态为终态，直接返回
        if(checkIsFinalStatus(settle.getBizSts())){
            return true;
        }

        // 向结算钱包发起查询
        AccountingInstrDO queryResult = commonManager.queryTransferStatus(accountingInstrDO);
        if(queryResult != null) {
            accountingInstrDO.setActgSts(queryResult.getActgSts());
            accountingInstrDO.setActgDt(queryResult.getActgDt());
            accountingInstrDO.setActgPrcCd(queryResult.getActgPrcCd());
            accountingInstrDO.setActgPrcInf(queryResult.getActgPrcInf());
        } else {
            accountingInstrDO.setActgSts(ActgStsEnum.FAILED.getCode());
            accountingInstrDO.setActgPrcCd(ClearingProdErrorEnum.UNKNOWN_EXCEPTION.getCode());
            accountingInstrDO.setActgPrcInf(ClearingProdErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        ChainTransDO orgChainTransDO = chainTransMapper.selectByMsgId(settle.getMsgId());
        if (Constant.DIRECTION_FROM_HLHT_TO_MBRIDGE.equals(orgChainTransDO.getMsgDrn())){
            dcepPresume(orgChainTransDO, accountingInstrDO, settle);
        } else {
            mcbsPresume(accountingInstrDO, settle);
        }

        return true;
    }

    @Override
    protected boolean process(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO,SettlementProdDO settle) {
        // 交易状态为终态，直接返回
        if(checkIsFinalStatus(settle.getBizSts())){
            return true;
        }

        ChainTransDO orgChainTransDO = chainTransMapper.selectByMsgId(settle.getMsgId());
        if (Constant.DIRECTION_FROM_HLHT_TO_MBRIDGE.equals(orgChainTransDO.getMsgDrn())){
            return dcepProcess(orgChainTransDO, accountingInstrDO, settle);
        } else {
            return mcbsProcess(accountingInstrDO, settle);
        }
    }

    private boolean dcepProcess(ChainTransDO orgChainTransDO, AccountingInstrDO accountingInstrDO, SettlementProdDO settle) {
        if (MsgTpEnum.CDT_REQUEST_ASYN.getCode().equals(accountingInstrDO.getMsgTp())){
            // 原交易为dcep203
            if(ClearingStatusEnum.ACCEPTED.getCode().equals(settle.getBizSts())){
                dcepRetry(orgChainTransDO, accountingInstrDO, settle);
            } else if(ClearingStatusEnum.WAIT_SETTLE.getCode().equals(settle.getBizSts())){
                invokeFinish(settle);
            }
        } else {
            // 原交易为dcep213
            if(ClearingStatusEnum.ACCEPTED.getCode().equals(settle.getBizSts())){
                invokeFinish(settle);
            }
        }

        return false;
    }

    private boolean mcbsProcess(AccountingInstrDO accountingInstrDO, SettlementProdDO settle) {
        if(ClearingStatusEnum.ACCEPTED.getCode().equals(settle.getBizSts())){
            mcbsRetry(accountingInstrDO, settle);
        }
        return false;
    }

    private void dcepPresume(ChainTransDO orgChainTransDO, AccountingInstrDO accountingInstrDO, SettlementProdDO settle) {
        dcepProcess(orgChainTransDO, accountingInstrDO, settle);
    }

    private void mcbsPresume(AccountingInstrDO accountingInstrDO, SettlementProdDO settle) {
        mcbsProcess(accountingInstrDO, settle);
    }

    private boolean checkIsFinalStatus(String bizSts) {
        if(ClearingStatusEnum.SETTLED.getCode().equals(bizSts)
                || ClearingStatusEnum.FAILED.getCode().equals(bizSts)
                || ClearingStatusEnum.CANCELLED.getCode().equals(bizSts)
                || ClearingStatusEnum.DAYEND_RETURN.getCode().equals(bizSts)){
            return true;
        }
        return false;
    }

    /**
     * 组mcbs204报文发送货币桥，应答mcbs203调用finishi接口
     * @param settle
     */
    private void invokeFinish(SettlementProdDO settle){
        // 报文转换
        Map<String, Object> convertMap = convertMessage(settle);
        if (convertMap == null) {
            return;
        }
        EnvelopeDTO<GwDTO> envelopeDTO = (EnvelopeDTO<GwDTO>) convertMap.get(KEY_ENVELOPE_DTO);
        GenericEnvelopeDTO<GenericGwDTO> genericEnvelopeDTO = (GenericEnvelopeDTO<GenericGwDTO>) convertMap.get(KEY_GENERIC_ENVELOPE_DTO);
        // 发送mcbs204
        GenericEnvelopeDTO<GenericGwDTO> resp = sendMbridge(envelopeDTO, genericEnvelopeDTO);
        if (resp == null){
            return;
        }
        dcepSndMbridgeService.finish(resp);
    }

    /**
     * 桥下交易transfer重试
     * @param orgChainTransDO
     * @param accountingInstrDO
     * @param settle
     */
    private void dcepRetry(ChainTransDO orgChainTransDO, AccountingInstrDO accountingInstrDO, SettlementProdDO settle){
        String orgMcbsMsgId = orgChainTransDO.getOutMsgId();
        // 报文转换
        Map<String, Object> convertMap = convertMessage(settle);
        if (convertMap == null) {
            return;
        }
        EnvelopeDTO<GwDTO> envelopeDTO = (EnvelopeDTO<GwDTO>) convertMap.get(KEY_ENVELOPE_DTO);
        GenericEnvelopeDTO<GenericGwDTO> genericEnvelopeDTO = (GenericEnvelopeDTO<GenericGwDTO>) convertMap.get(KEY_GENERIC_ENVELOPE_DTO);
        // transfer重试
        mbridgeManager.hlht2MbridgeAsync(genericEnvelopeDTO, envelopeDTO, accountingInstrDO, orgMcbsMsgId);
    }

    /**
     * 桥上交易transfer重试
     * @param accountingInstrDO
     * @param settle
     */
    private void mcbsRetry(AccountingInstrDO accountingInstrDO, SettlementProdDO settle){
        // 报文转换
        Map<String, Object> convertMap = convertMessage(settle);
        if (convertMap == null) {
            return;
        }
        EnvelopeDTO<GwDTO> envelopeDTO = (EnvelopeDTO<GwDTO>) convertMap.get(KEY_ENVELOPE_DTO);
        GenericEnvelopeDTO<GenericGwDTO> genericEnvelopeDTO = (GenericEnvelopeDTO<GenericGwDTO>) convertMap.get(KEY_GENERIC_ENVELOPE_DTO);
        // transfer重试
        mbridgeManager.mBridge2HlhtAsync(genericEnvelopeDTO, envelopeDTO, accountingInstrDO);
    }

    /**
     * 将交易请求报文转换为 EnvelopeDTO 和 GenericEnvelopeDTO
     * @param settle 结算产品信息
     * @return 一个包含 EnvelopeDTO 和 GenericEnvelopeDTO 的 Map
     */
    private Map<String, Object> convertMessage(SettlementProdDO settle) {
        // 查询交易档案表获取原交易请求报文
        CommonRecordDO commonRecordDO = commonRecordMapper.selectByPrimaryKey(new CommonRecordDO(settle.getMsgId(), settle.getMsgTp()));
        String orgMsgTp = StringUtils.isNotBlank(commonRecordDO.getOrgMsgTp()) ? commonRecordDO.getOrgMsgTp() : settle.getMsgTp();
        TypeReference typeReference = map.get(orgMsgTp);
        Object object = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(), typeReference);
        GenericEnvelopeDTO<GenericGwDTO> genericEnvelopeDTO;
        EnvelopeDTO<GwDTO> envelopeDTO;

        if (object instanceof McbsEnvelopeDTO) {
            genericEnvelopeDTO = (McbsEnvelopeDTO<McbsGwDTO>) object;
            // 报文转换
            envelopeDTO = mbridgeToDcepConvertManager.convertRequest(genericEnvelopeDTO);
        } else if (object instanceof EnvelopeDTO) {
            envelopeDTO = (EnvelopeDTO<GwDTO>) object;
            // 报文转换
            genericEnvelopeDTO = dcep2mBridge.convertRequest(envelopeDTO);
        } else {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        result.put(KEY_ENVELOPE_DTO, envelopeDTO);
        result.put(KEY_GENERIC_ENVELOPE_DTO, genericEnvelopeDTO);
        return result;
    }

    private GenericEnvelopeDTO<GenericGwDTO> sendMbridge(EnvelopeDTO<GwDTO> envelopeDTO, GenericEnvelopeDTO<GenericGwDTO> genericEnvelopeDTO){
        ClearingDTO clearingDTO = (ClearingDTO) envelopeDTO.body();
        GenericEnvelopeDTO<GenericGwDTO> mcbs204EnvelopeDTO = DtoUtil.assemblyMcbs204(genericEnvelopeDTO, clearingDTO.clrMbridgeInf());
        Response<GenericEnvelopeDTO<GenericGwDTO>> genericGwResp;
        try {
            log.info("mBridgeGwoutService request:{}", mcbs204EnvelopeDTO);
            genericGwResp = mBridgeGwoutService.execute(mcbs204EnvelopeDTO);
            if (!genericGwResp.isSuccess()){
                log.error("invocation mBridgeGwoutService failed, errCode={}, errMsg={}", genericGwResp.getErrorCode(), genericGwResp.getErrorMsg());
                return null;
            }
            log.info("mBridgeGwoutService response:{}", genericGwResp);
        } catch (Exception e) {
            log.info(Constant.INTERNAL_ERROR_MSG, e);
            return null;
        }
        if (genericGwResp.getResult() == null){
            log.info("mBridgeGwoutService response null");
            return null;
        }
        return genericGwResp.getResult();
    }
}
