package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CheckUtil;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.common.utils.ErrorCodeUtil;
import com.dcep.dips.wholesalepayment.common.utils.MsgIdUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.ChainTransMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.ChainTransDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dto.dc203.Dcep20301001DTO;
import com.dcep.dips.wholesalepayment.dto.dc213.Dcep21301001DTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.dips.wholesalepayment.enums.McbsStatusEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeToDcepConvertManager;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs200Envelope;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.dips.wholesalepayment.utils.TestUtils;
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
public class McbsSndMbridgeServiceImplTest {

    @Mock
    private MbridgeManager mbridgeManager;

    @Mock
    private SettlementProdMapper settlementProdMapper;

    @Mock
    private ChainTransMapper chainTransMapper;

    @Mock
    private AccountingInstrMapper accountingInstrMapper;

    @Mock
    private MbridgeToDcepConvertManager mbridgeToDcepConvertManager;

    @Mock
    private CommonManager commonManager;

    @InjectMocks
    private McbsSndMbridgeServiceImpl mcbsSndMbridgeServiceImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void process_BusinessCheck_Fail() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(false);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genDcepMsgId(any(),any()))
                    .thenReturn("20251016070000042064686501414000");

            GenericEnvelopeDTO<GenericGwDTO> genericGwDTOGenericEnvelopeDTO = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(mbridgeToDcepConvertManager.convertRequest(any())).thenReturn(gwReqDTO);

            Response<GenericEnvelopeDTO<GenericGwDTO>> response = mcbsSndMbridgeServiceImpl.process(genericGwDTOGenericEnvelopeDTO);
            Mcbs90000101DTO body = (Mcbs90000101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(McbsStatusEnum.FAIL.getCode(), body.getReceiptDetails().getRequestHandling().getStatusCode());
        }
    }

    @Test
    public void process_BusinessCheck_Fail1() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genDcepMsgId(any(),any()))
                    .thenReturn("20251016070000042064686501414000");

            GenericEnvelopeDTO<GenericGwDTO> genericGwDTOGenericEnvelopeDTO = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(mbridgeToDcepConvertManager.convertRequest(any())).thenReturn(gwReqDTO);

            Response<GenericEnvelopeDTO<GenericGwDTO>> response = mcbsSndMbridgeServiceImpl.process(genericGwDTOGenericEnvelopeDTO);
            Mcbs90000101DTO body = (Mcbs90000101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(McbsStatusEnum.FAIL.getCode(), body.getReceiptDetails().getRequestHandling().getStatusCode());
        }
    }

    @Test
    public void process_DuplicateKeyException() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genDcepMsgId(any(),any()))
                    .thenReturn("20251016070000042064686501414000");

            GenericEnvelopeDTO<GenericGwDTO> genericGwDTOGenericEnvelopeDTO = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(mbridgeToDcepConvertManager.convertRequest(any())).thenReturn(gwReqDTO);
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_THREE);
            doThrow(DuplicateKeyException.class).when(mbridgeManager).prepare(any(), any());

            Response<GenericEnvelopeDTO<GenericGwDTO>> response = mcbsSndMbridgeServiceImpl.process(genericGwDTOGenericEnvelopeDTO);
            Mcbs90000101DTO body = (Mcbs90000101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(McbsStatusEnum.FAIL.getCode(), body.getReceiptDetails().getRequestHandling().getStatusCode());
        }
    }

    @Test
    public void process_DuplicateKeyException1() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genDcepMsgId(any(),any()))
                    .thenReturn("20251016070000042064686501414000");

            GenericEnvelopeDTO<GenericGwDTO> genericGwDTOGenericEnvelopeDTO = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(mbridgeToDcepConvertManager.convertRequest(any())).thenReturn(gwReqDTO);
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_ONE);
            doThrow(DuplicateKeyException.class).when(mbridgeManager).prepare(any(), any());

            Response<GenericEnvelopeDTO<GenericGwDTO>> response = mcbsSndMbridgeServiceImpl.process(genericGwDTOGenericEnvelopeDTO);
            Mcbs90000101DTO body = (Mcbs90000101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(McbsStatusEnum.FAIL.getCode(), body.getReceiptDetails().getRequestHandling().getStatusCode());
        }
    }

    @Test
    public void process_DuplicateKeyException2() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class);
             MockedStatic<CheckUtil> mockedCheckUtil = Mockito.mockStatic(CheckUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genDcepMsgId(any(),any()))
                    .thenReturn("20251016070000042064686501414000");
            mockedCheckUtil.when(() -> CheckUtil.idempotentMatch(any(),any(),any()))
                    .thenReturn(false);

            GenericEnvelopeDTO<GenericGwDTO> genericGwDTOGenericEnvelopeDTO = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(mbridgeToDcepConvertManager.convertRequest(any())).thenReturn(gwReqDTO);
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_TWO);
            doThrow(DuplicateKeyException.class).when(mbridgeManager).prepare(any(), any());
            ChainTransDO chainTransDO = new ChainTransDO();
            chainTransDO.setMsgId("20251016070000042064686501414000");
            when(chainTransMapper.selectByPrimaryKey(any())).thenReturn(chainTransDO);
            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(new SettlementProdDO());

            Response<GenericEnvelopeDTO<GenericGwDTO>> response = mcbsSndMbridgeServiceImpl.process(genericGwDTOGenericEnvelopeDTO);
            Mcbs90000101DTO body = (Mcbs90000101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(McbsStatusEnum.FAIL.getCode(), body.getReceiptDetails().getRequestHandling().getStatusCode());
            assertEquals(ErrorCodeUtil.getMbridgeStsRsnInfAddtlInf(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode()), body.getReceiptDetails().getRequestHandling().getDescription());
        }
    }

    @Test
    public void process_succ() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class);
             MockedStatic<CommonUtil> mockedCommonUtil = Mockito.mockStatic(CommonUtil.class);
             MockedStatic<MsgIdUtil> mockedMsgIdUtil = Mockito.mockStatic(MsgIdUtil.class);
             MockedStatic<CheckUtil> mockedCheckUtil = Mockito.mockStatic(CheckUtil.class)) {
            mockedCommonUtil.when(CommonUtil::getEnvVal)
                    .thenReturn("1");
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedMsgIdUtil.when(() -> MsgIdUtil.genDcepMsgId(any(),any()))
                    .thenReturn("20251016070000042064686501414000");
            mockedCheckUtil.when(() -> CheckUtil.idempotentMatch(any(),any(),any()))
                    .thenReturn(true);

            GenericEnvelopeDTO<GenericGwDTO> genericGwDTOGenericEnvelopeDTO = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
            EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
            Dcep20301001DTO body1 = (Dcep20301001DTO) gwReqDTO.body();
            body1.setMbridgeReqDTO(generateMbridgeReqDTO());
            when(mbridgeToDcepConvertManager.convertRequest(any())).thenReturn(gwReqDTO);
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_TWO);
            doThrow(DuplicateKeyException.class).when(mbridgeManager).prepare(any(), any());
            ChainTransDO chainTransDO = new ChainTransDO();
            chainTransDO.setMsgId("20251016070000042064686501414000");
            when(chainTransMapper.selectByPrimaryKey(any())).thenReturn(chainTransDO);
            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(new SettlementProdDO());
            when(accountingInstrMapper.selectByMsgId(any())).thenReturn(new AccountingInstrDO());

            Response<GenericEnvelopeDTO<GenericGwDTO>> response = mcbsSndMbridgeServiceImpl.process(genericGwDTOGenericEnvelopeDTO);
            Mcbs90000101DTO body = (Mcbs90000101DTO) response.getResult().body();
            assertTrue(response.isSuccess());
            assertEquals(McbsStatusEnum.RSVL.getCode(), body.getReceiptDetails().getRequestHandling().getStatusCode());
        }
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
        mbridgeReqDTO.setSndDtTm(TestUtils.getCurCreDtTm());
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
