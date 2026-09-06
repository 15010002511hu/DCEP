package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep90000101DTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CheckUtil;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.common.utils.MsgIdUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.*;
import com.dcep.dips.wholesalepayment.dal.model.*;
import com.dcep.dips.wholesalepayment.dto.dc203.Dcep20301001DTO;
import com.dcep.dips.wholesalepayment.dto.dc213.Dcep21301001DTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.DcepToMbridgeConvertManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeManager;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs200Envelope;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.dips.wholesalepayment.utils.TestUtils;
import com.dcep.gateway.mcbdc.dto.common.GroupHeader;
import com.dcep.gateway.mcbdc.dto.common.OriginGroupInfoAndStatus;
import com.dcep.gateway.mcbdc.dto.mcbs203.Mcbs20300101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs203.TransactionInfoAndStatus;
import com.dcep.gateway.mcbdc.dto.mcbs203.TransactionInfoAndStatusSupplementaryData;
import com.dcep.gateway.mcbdc.dto.mcbs900.Mcbs90000101DTO;
import com.dcep.gateway.mcbdc.dto.soap.*;
import org.apache.dubbo.rpc.RpcContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DcepSndMbridgeServiceImplTest {

    @Mock
    private MbridgeManager mbridgeManager;

    @Mock
    private SettlementProdMapper settlementProdMapper;

    @Mock
    private ChainTransMapper chainTransMapper;

    @Mock
    private AccountingManager accountingManager;

    @Mock
    private AccountingInstrMapper accountingInstrMapper;

    @Mock
    private ZerooutCtrlDOMapper zerooutCtrlDOMapper;

    @Mock
    DcepToMbridgeConvertManager dcep2mBridge;

    @Mock
    private CommonManager commonManager;

    @InjectMocks
    private DcepSndMbridgeServiceImpl dcepSndMbridgeServiceImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void finish_InvalidBodyType_ReturnsError() throws DcepException {
        GenericEnvelopeDTO<GenericGwDTO> genericGwDTOGenericEnvelopeDTO = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
        Response<GenericEnvelopeDTO<GenericGwDTO>> response = dcepSndMbridgeServiceImpl.finish(genericGwDTOGenericEnvelopeDTO);
        Mcbs90000101DTO body = (Mcbs90000101DTO) response.getResult().body();

        assertTrue(response.isSuccess());
        assertEquals(McbsStatusEnum.FAIL.getCode(), body.getReceiptDetails().getRequestHandling().getStatusCode());
    }

    @Test
    public void finish_NoChainTransDO_ReturnsError() throws DcepException {
        String orgMcbsMsgId = "20251016070000042064686501414000";
        String orgMsgTp = "dcep.213.010.01";

        when(chainTransMapper.selectByPrimaryKey(orgMcbsMsgId)).thenReturn(null);

        GenericEnvelopeDTO<GenericGwDTO> req = generateMcbs203EnvelopeDTO(orgMcbsMsgId, orgMsgTp, "SUCD");

        Response<GenericEnvelopeDTO<GenericGwDTO>> response = dcepSndMbridgeServiceImpl.finish(req);
        Mcbs90000101DTO body = (Mcbs90000101DTO) response.getResult().body();

        assertTrue(response.isSuccess());
        assertEquals(McbsStatusEnum.FAIL.getCode(), body.getReceiptDetails().getRequestHandling().getStatusCode());
    }

    @Test
    public void finish_SettlementProdStatusSettledOrFailed_ReturnsSuccess() throws DcepException {
        String orgMcbsMsgId = "20251016070000042064686501414000";
        String orgMsgTp = "dcep.213.010.01";

        ChainTransDO chainTransDO = new ChainTransDO();
        chainTransDO.setMsgId("20251016070000042064686501414000");

        when(chainTransMapper.selectByPrimaryKey(orgMcbsMsgId)).thenReturn(chainTransDO);

        SettlementProdDO orgSettlementProdDO = new SettlementProdDO();
        orgSettlementProdDO.setBizSts(ClearingStatusEnum.SETTLED.getCode());

        when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(orgSettlementProdDO);

        GenericEnvelopeDTO<GenericGwDTO> req = generateMcbs203EnvelopeDTO(orgMcbsMsgId, orgMsgTp, "SUCD");

        Response<GenericEnvelopeDTO<GenericGwDTO>> response = dcepSndMbridgeServiceImpl.finish(req);

        assertTrue(response.isSuccess());
        assertNotNull(response.getResult());
    }

    @Test
    public void finish_OriginTransactionOnBridge_Success() throws DcepException {
        String orgMcbsMsgId = "20251016070000042064686501414000";
        String orgMsgTp = "dcep.203.010.01";

        ChainTransDO chainTransDO = new ChainTransDO();
        chainTransDO.setMsgId("20251016070000042064686501414000");

        when(chainTransMapper.selectByPrimaryKey(orgMcbsMsgId)).thenReturn(chainTransDO);

        SettlementProdDO settlementProdDO = new SettlementProdDO();
        settlementProdDO.setBizSts("OTHER_STATUS");
        settlementProdDO.setMsgTp(orgMsgTp);

        when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);

        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        when(accountingInstrMapper.selectByMsgIdPrepare(any())).thenReturn(accountingInstrDO);
        doNothing().when(mbridgeManager).pendingFinish(any(), any(), any(), any(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any(), any());

        GenericEnvelopeDTO<GenericGwDTO> req = generateMcbs203EnvelopeDTO(orgMcbsMsgId, orgMsgTp, "SUCD");

        Response<GenericEnvelopeDTO<GenericGwDTO>> response = dcepSndMbridgeServiceImpl.finish(req);

        assertTrue(response.isSuccess());
        assertNotNull(response.getResult());
    }

    @Test
    public void finish_OriginTransactionOnBridge_Success1() throws DcepException {
        String orgMcbsMsgId = "20251016070000042064686501414000";
        String orgMsgTp = "dcep.203.010.01";

        ChainTransDO chainTransDO = new ChainTransDO();
        chainTransDO.setMsgId("20251016070000042064686501414000");

        when(chainTransMapper.selectByPrimaryKey(orgMcbsMsgId)).thenReturn(chainTransDO);

        SettlementProdDO settlementProdDO = new SettlementProdDO();
        settlementProdDO.setBizSts("OTHER_STATUS");
        settlementProdDO.setMsgTp(orgMsgTp);

        when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);

        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setBizDt("20251013");
        accountingInstrDO.setFromWlltId("1111");
        accountingInstrDO.setToWlltId("1111");
        accountingInstrDO.setTransId(IdUtils.randomTransIdWithBizDt("20251020"));
        when(accountingInstrMapper.selectByMsgIdPrepare(any())).thenReturn(accountingInstrDO);

        TransferRespDTO transferRespDTO = new TransferRespDTO();
        transferRespDTO.setAccountingStatus("0");
        transferRespDTO.setAccountingDate("20251013");
        when(accountingInstrMapper.insert(any())).thenReturn(1);
        doNothing().when(mbridgeManager).transfer(any(),anyBoolean());
//        doNothing().when(mbridgeManager).pendingFinish(any(), any(), any(), any(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any(), any());

        GenericEnvelopeDTO<GenericGwDTO> req = generateMcbs203EnvelopeDTO(orgMcbsMsgId, orgMsgTp, "FAIL");

        Response<GenericEnvelopeDTO<GenericGwDTO>> response = dcepSndMbridgeServiceImpl.finish(req);

        assertTrue(response.isSuccess());
        assertNotNull(response.getResult());
    }

    @Test
    public void finish_OriginTransactionOffBridge_Success() throws DcepException {
        String orgMcbsMsgId = "20251016070000042064686501414000";
        String orgMsgTp = "dcep.213.010.01";

        ChainTransDO chainTransDO = new ChainTransDO();
        chainTransDO.setMsgId("20251016070000042064686501414000");

        when(chainTransMapper.selectByPrimaryKey(orgMcbsMsgId)).thenReturn(chainTransDO);

        SettlementProdDO settlementProdDO = new SettlementProdDO();
        settlementProdDO.setBizSts("OTHER_STATUS");
        settlementProdDO.setMsgTp(orgMsgTp);

        when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);

        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setBizDt("20251013");
        accountingInstrDO.setFromWlltId("1111");
        accountingInstrDO.setToWlltId("1111");
        accountingInstrDO.setActgSts("0");
        when(accountingInstrMapper.selectByMsgIdPrepare(any())).thenReturn(accountingInstrDO);

        TransferRespDTO transferRespDTO = new TransferRespDTO();
        transferRespDTO.setAccountingStatus("0");
        transferRespDTO.setAccountingDate("20251013");
//        Response<TransferRespDTO> transferRespDTOResponse = new Response<>(transferRespDTO);
        doNothing().when(mbridgeManager).transfer(any(),anyBoolean());
        doNothing().when(mbridgeManager).pendingFinish(any(), any(), any(), any(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any(), any());

        GenericEnvelopeDTO<GenericGwDTO> req = generateMcbs203EnvelopeDTO(orgMcbsMsgId, orgMsgTp, "SUCD");

        Response<GenericEnvelopeDTO<GenericGwDTO>> response = dcepSndMbridgeServiceImpl.finish(req);

        assertTrue(response.isSuccess());
        assertNotNull(response.getResult());
    }

    @Test
    public void finish_OriginTransactionOffBridge_Success1() throws DcepException {
        String orgMcbsMsgId = "20251016070000042064686501414000";
        String orgMsgTp = "dcep.213.010.01";

        ChainTransDO chainTransDO = new ChainTransDO();
        chainTransDO.setMsgId("20251016070000042064686501414000");

        when(chainTransMapper.selectByPrimaryKey(orgMcbsMsgId)).thenReturn(chainTransDO);

        SettlementProdDO settlementProdDO = new SettlementProdDO();
        settlementProdDO.setBizSts("OTHER_STATUS");
        settlementProdDO.setMsgTp(orgMsgTp);

        when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);

        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        when(accountingInstrMapper.selectByMsgIdPrepare(any())).thenReturn(accountingInstrDO);
        doNothing().when(mbridgeManager).pendingFinish(any(), any(), any(), any(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any(), any());

        GenericEnvelopeDTO<GenericGwDTO> req = generateMcbs203EnvelopeDTO(orgMcbsMsgId, orgMsgTp, "FAIL");

        Response<GenericEnvelopeDTO<GenericGwDTO>> response = dcepSndMbridgeServiceImpl.finish(req);

        assertTrue(response.isSuccess());
        assertNotNull(response.getResult());
    }

    @Test
    public void prepare_BusinessCheck_Fail() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(false);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genMcbsMsgId(any()))
                    .thenReturn("20251016070000042064686501414000");

            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(dcep2mBridge.convertRequest(any())).thenReturn(null);

            Response<EnvelopeDTO<GwDTO>> response = dcepSndMbridgeServiceImpl.prepare(gwReqDTO);
            Dcep90000101DTO body = (Dcep90000101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(ClearingStatusEnum.FAILED.getCode(), body.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    public void prepare_BusinessCheck_Fail1() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genMcbsMsgId(any()))
                    .thenReturn("20251016070000042064686501414000");

            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(dcep2mBridge.convertRequest(any())).thenReturn(null);

            Response<EnvelopeDTO<GwDTO>> response = dcepSndMbridgeServiceImpl.prepare(gwReqDTO);
            Dcep90000101DTO body = (Dcep90000101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(ClearingStatusEnum.FAILED.getCode(), body.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    public void prepare_BusinessCheck_Fail12() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genMcbsMsgId(any()))
                    .thenReturn("20251016070000042064686501414000");

            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(dcep2mBridge.convertRequest(any())).thenReturn(null);

            ZerooutCtrlDO zerooutCtrlDO = ZerooutCtrlDO.builder().
                    prcSts(ZerooutPrcStsEnum.PROCESS.getCode()).
                    build();
            when(zerooutCtrlDOMapper.selecPrcSts(any(), any())).thenReturn(zerooutCtrlDO);
            when(commonManager.checkSystemDate(any())).thenReturn(true);

            Response<EnvelopeDTO<GwDTO>> response = dcepSndMbridgeServiceImpl.prepare(gwReqDTO);
            Dcep90000101DTO body = (Dcep90000101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(ClearingStatusEnum.FAILED.getCode(), body.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    public void prepare_DuplicateKeyException() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genMcbsMsgId(any()))
                    .thenReturn("20251016070000042064686501414000");

            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(dcep2mBridge.convertRequest(any())).thenReturn(null);

            ZerooutCtrlDO zerooutCtrlDO = ZerooutCtrlDO.builder().
                    prcSts(ZerooutPrcStsEnum.SUCCESS.getCode()).
                    build();
            when(zerooutCtrlDOMapper.selecPrcSts(any(), any())).thenReturn(zerooutCtrlDO);
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_THREE);
            doThrow(DuplicateKeyException.class).when(mbridgeManager).prepare(any(), any());

            Response<EnvelopeDTO<GwDTO>> response = dcepSndMbridgeServiceImpl.prepare(gwReqDTO);
            Dcep91100101DTO body = (Dcep91100101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(), body.getFaultcode());
        }
    }

    @Test
    public void prepare_DuplicateKeyException1() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genMcbsMsgId(any()))
                    .thenReturn("20251016070000042064686501414000");

            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(dcep2mBridge.convertRequest(any())).thenReturn(null);

            ZerooutCtrlDO zerooutCtrlDO = ZerooutCtrlDO.builder().
                    prcSts(ZerooutPrcStsEnum.SUCCESS.getCode()).
                    build();
            when(zerooutCtrlDOMapper.selecPrcSts(any(), any())).thenReturn(zerooutCtrlDO);
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_TWO);
            doThrow(DuplicateKeyException.class).when(mbridgeManager).prepare(any(), any());

            Response<EnvelopeDTO<GwDTO>> response = dcepSndMbridgeServiceImpl.prepare(gwReqDTO);
            Dcep91100101DTO body = (Dcep91100101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(), body.getFaultcode());
        }
    }

    @Test
    public void prepare_DuplicateKeyException2() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class);
             MockedStatic<CheckUtil> mockedCheckUtil = Mockito.mockStatic(CheckUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genMcbsMsgId(any()))
                    .thenReturn("20251016070000042064686501414000");
            mockedCheckUtil.when(() -> CheckUtil.idempotentMatch(any(),any(),any()))
                    .thenReturn(false);

            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(dcep2mBridge.convertRequest(any())).thenReturn(null);

            ZerooutCtrlDO zerooutCtrlDO = ZerooutCtrlDO.builder().
                    prcSts(ZerooutPrcStsEnum.SUCCESS.getCode()).
                    build();
            when(zerooutCtrlDOMapper.selecPrcSts(any(), any())).thenReturn(zerooutCtrlDO);
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_ONE);
            doThrow(DuplicateKeyException.class).when(mbridgeManager).prepare(any(), any());
            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(new SettlementProdDO());

            Response<EnvelopeDTO<GwDTO>> response = dcepSndMbridgeServiceImpl.prepare(gwReqDTO);
            Dcep91100101DTO body = (Dcep91100101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(), body.getFaultcode());
        }
    }

    @Test
    public void prepare_succ() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class);
             MockedStatic<CheckUtil> mockedCheckUtil = Mockito.mockStatic(CheckUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genMcbsMsgId(any()))
                    .thenReturn("20251016070000042064686501414000");
            mockedCheckUtil.when(() -> CheckUtil.idempotentMatch(any(),any(),any()))
                    .thenReturn(false);

            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(dcep2mBridge.convertRequest(any())).thenReturn(null);

            ZerooutCtrlDO zerooutCtrlDO = ZerooutCtrlDO.builder().
                    prcSts(ZerooutPrcStsEnum.SUCCESS.getCode()).
                    build();
            when(zerooutCtrlDOMapper.selecPrcSts(any(), any())).thenReturn(zerooutCtrlDO);
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(mbridgeManager.prepare(any(), any())).thenReturn(null);

            Response<EnvelopeDTO<GwDTO>> response = dcepSndMbridgeServiceImpl.prepare(gwReqDTO);

            verify(mbridgeManager).hlht2MbridgeAsync(any(), any(), any(), any());

            Dcep90000101DTO body = (Dcep90000101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(ClearingStatusEnum.ACCEPTED.getCode(), body.getCmonConfInf().getPrcSts());
        }
    }

    private GenericEnvelopeDTO<GenericGwDTO> generateMcbs203EnvelopeDTO(String orgMcbsMsgId, String orgMsgTp, String statusId) {
        GroupHeader groupHeader = new GroupHeader();
        groupHeader.setMsgId(generateRandomString());
        groupHeader.setCreateDateTime(TestUtils.getCurDate());

        OriginGroupInfoAndStatus originGroupInfoAndStatus = new OriginGroupInfoAndStatus();
        originGroupInfoAndStatus.setOriginMsgId(orgMcbsMsgId);
        originGroupInfoAndStatus.setOriginMsgName(orgMsgTp);

        TransactionInfoAndStatus transactionInfoAndStatus = new TransactionInfoAndStatus();
        transactionInfoAndStatus.setStatusId(statusId);
        transactionInfoAndStatus.setTransactionInfoAndStatusSupplementaryData(new TransactionInfoAndStatusSupplementaryData(Constant.MBRIDGE_203_PLACE_AND_NAME, null));
        Mcbs20300101DTO mcbs20300101DTO = Mcbs20300101DTO.builder()
                .grpHdr(groupHeader)
                .originGroupInfoAndStatus(originGroupInfoAndStatus)
                .transactionInfoAndStatus(transactionInfoAndStatus)
                .build();

        McbsEnvelopeDTO<McbsGwDTO> mcbsGwReqDTO = new McbsEnvelopeDTO<>();
        mcbsGwReqDTO.setSoapBody(new McbsSoapBody<>(mcbs20300101DTO));
        mcbsGwReqDTO.setSoapHeader(TestUtils.generateMcbsSoapHeader(orgMsgTp));
        return mcbsGwReqDTO;
    }

    public EnvelopeDTO<GwDTO> loadEnvelopeDTO_203() {
        String msgTp = "dcep.203.010.01";
        String shortMsgTp = "203";
        String shortSender = "002";
        String shortReceiver = "005";
        String msgId = TestUtils.getMsgId(shortSender, shortMsgTp);
        String creDtTm = TestUtils.getCurCreDtTm();
        String xmlFile = "mbridge/20301.xml";

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader soapHeader = TestUtils.createSoapHeader(msgTp, msgId, creDtTm, shortSender, shortReceiver);
        envelopeDTO.setSoapHeader(soapHeader);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{BatchId}}", getBatchId());

        Dcep20301001DTO dcep203DTO = TestUtils.createDcepDTO(xmlFile, dataMap, Dcep20301001DTO.class);
        envelopeDTO.setSoapBody(new SoapBody<>(dcep203DTO));
        return envelopeDTO;
    }

    public EnvelopeDTO<GwDTO> loadEnvelopeDTO_213() {
        String msgTp = "dcep.213.010.01";
        String shortMsgTp = "213";
        String shortSender = "005";
        String shortReceiver = "002";
        String msgId = TestUtils.getMsgId(shortSender, shortMsgTp);
        String creDtTm = TestUtils.getCurCreDtTm();
        String xmlFile = "mbridge/21301.xml";

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader soapHeader = TestUtils.createSoapHeader(msgTp, msgId, creDtTm, shortSender, shortReceiver);
        envelopeDTO.setSoapHeader(soapHeader);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{BatchId}}", getBatchId());

        Dcep21301001DTO dcep213DTO = TestUtils.createDcepDTO(xmlFile, dataMap, Dcep21301001DTO.class);
        envelopeDTO.setSoapBody(new SoapBody<>(dcep213DTO));
        return envelopeDTO;
    }

    private MbridgeReqDTO generateMbridgeReqDTO() {
        MbridgeReqDTO mbridgeReqDTO = new MbridgeReqDTO();
        mbridgeReqDTO.setMcbsMsgId(generateRandomString());
        mbridgeReqDTO.setSndDtTm(generateRandomString());
        mbridgeReqDTO.setMcbsBatId(generateRandomString());
        mbridgeReqDTO.setMcbsMsgTp(generateRandomString());
        mbridgeReqDTO.setMsgDrn(generateRandomString());
        mbridgeReqDTO.setClrTp(generateRandomString());
        mbridgeReqDTO.setSenderPtyId(generateRandomString());
        mbridgeReqDTO.setSenderLEI(generateRandomString());
        mbridgeReqDTO.setReceiverPtyId(generateRandomString());
        mbridgeReqDTO.setReceiverLEI(generateRandomString());
        mbridgeReqDTO.setDbtrPtyId(generateRandomString());
        mbridgeReqDTO.setDbtrPtyLEI(generateRandomString());
        mbridgeReqDTO.setCdtrPtyId(generateRandomString());
        mbridgeReqDTO.setCdtrPtyLEI(generateRandomString());
        mbridgeReqDTO.setDbtrWltId(generateRandomString());
        mbridgeReqDTO.setCdtrWltId(generateRandomString());
        mbridgeReqDTO.setParameterId(generateRandomString());
        mbridgeReqDTO.setMcbsOrgMsgId(generateRandomString());
        mbridgeReqDTO.setPrcSts(generateRandomString());
        mbridgeReqDTO.setShardingMsgId(generateRandomString());
        mbridgeReqDTO.setSndChnlSys(generateRandomString());
        mbridgeReqDTO.setRcvChnlSys(generateRandomString());
        return mbridgeReqDTO;

    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(22);
        for (int i = 0; i < 22; i++) {
            sb.append(random.nextInt(10)); // 生成0-9之间的随机数字
        }
        return getSysTime("yyyyMMddHH") + sb;
    }

    private String getSysTime(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getBatchId() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = sdf.format(date);

        // 解析日期时间字符串
        String[] parts = formattedDate.split("[T:-]");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        int hour = Integer.parseInt(parts[3]);

        // 生成批次ID
        return "B" + year + s(month + 1) + s(day) + s(hour + 1) + "00";
    }

    private static String s(int num) {
        return String.format("%02d", num);
    }
}
