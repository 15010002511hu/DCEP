package com.dcep.dips.wholesalepayment.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.common.utils.MsgIdUtil;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.common.constant.DatePattern;
import com.dcep.dips.wholesalepayment.api.ChainService;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CheckUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.*;
import com.dcep.dips.wholesalepayment.dal.model.*;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustRespDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.ChainManager;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.SettlementManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
public class ChainServiceImplTest {
    @InjectMocks
    private ChainServiceImpl chainService;

    @Mock
    private ChainManager chainManager;

    @Mock
    private SettlementProdMapper settlementProdMapper;

    @Mock
    private AccountingInstrMapper accountingInstrMapper;

    @Mock
    private AccountingManager accountingManager;

    @Mock
    private ZerooutCtrlDOMapper zerooutCtrlDOMapper;

    @Mock
    private SystemStatusDOMapper systemStatusDOMapper;

    @Mock
    private CommonManager commonManager;

    @Mock
    private SettlementManager settlementManager;

    @Mock
    private CommonRecordMapper commonRecordMapper;
//    @Mock
//    private  OrgCache cache;

//    private  Field orgCache;

    private MockedStatic<InfoCacheUtil> infoCacheUtil;
    private MockedStatic<CheckUtil> checkUtil;

    @Before
    public void setUp() throws NoSuchFieldException {
        MockitoAnnotations.openMocks(this);
//        ReflectionTestUtils.setField(chainService, "CREDTTM_INTERVAL", new Long(10*60*1000).toString());
//        ReflectionTestUtils.setField(chainService, "MSGID_DATE_INTERVAL", "300");
        ReflectionTestUtils.setField(chainService, "MSGID_DATE_INTERVAL", "300");
//        orgCache = InfoCacheUtil.class.getDeclaredField("orgCache");
//        orgCache.setAccessible(true);

        infoCacheUtil = Mockito.mockStatic(InfoCacheUtil.class);
        checkUtil = Mockito.mockStatic(CheckUtil.class);

    }
    @After
    public void testDown() throws NoSuchFieldException {
        // 清理静态模拟
        infoCacheUtil.close();
        checkUtil.close();

    }


    /**
     * 调用dubbo接口测试
     */
//    @Test
    public void onChainAdjustReal(){
        ReferenceConfig<ChainService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(ChainService.class);
        referenceConfig.setValidation("false");
        referenceConfig.setUrl("dubbo://127.0.0.1:20883");
        referenceConfig.setApplication(new ApplicationConfig("wholesalepayment"));
        referenceConfig.setRetries(0);
        referenceConfig.setCheck(true);
        ChainService chainService1 = referenceConfig.get();

        OnChainAdjustReqDTO req = createOnChainAdjustReqDTO();
        try {
            Response<OnChainAdjustRespDTO> response = chainService1.onChainAdjust(req);
            log.info("response:"+JSON.toJSONString(response));
        }catch (Exception e){
           e.printStackTrace();
           log.debug("异常信息：",e.getMessage());
        }

    }


    public OnChainAdjustReqDTO createOnChainAdjustReqDTO(){
        OnChainAdjustReqDTO req = new OnChainAdjustReqDTO();
        req.setMsgId((DateUtil.format(new Date(),"yyyyMMdd")+ "203" + "1" + "203" + MsgIdUtil.getRandomNum(14)) + "00" + "0");
        req.setMsgId("20251027"+ "203" + "1" + "203" + MsgIdUtil.getRandomNum(14) + "00" + "0");
//        req.setMsgId("20251015203120347452823150745000");
        req.setClearingMemberId("C1115332000018");
        req.setClearingSystemId("BCSP");
        req.setClearingWalletId("1000001147");
        req.setExpectedSettlementDate("20251027");
        req.setBatchId("B"+DateUtil.format(new Date(),"yyyyMMdd")+DateUtil.format(new Date(),"HH")+"00" );
        req.setBizTp("719");
        req.setBizKind(ActgBizKindEnum.DEFAULT.getCode());
        req.setAdjustTp("QOT01");
        req.setCurrency("CNY");
        req.setAmount(new BigDecimal("22"));
        req.setUseCurrentSystemFlag("A");
        req.setRemark("备注");
        return req;
    }

//    @Test
    public void testEndReturn() {

        ReferenceConfig<ChainService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(ChainService.class);
        referenceConfig.setValidation("false");
        referenceConfig.setUrl("dubbo://127.0.0.1:20883");
        referenceConfig.setApplication(new ApplicationConfig("wholesalepayment"));
        referenceConfig.setRetries(0);
        referenceConfig.setCheck(true);
        ChainService chainService1 = referenceConfig.get();

        try {
            //chainService1.endReturn("20251018");
        }catch (Exception e){
            e.printStackTrace();
            log.debug("异常信息：",e.getMessage());
        }
    }

    @Test
    @DisplayName("测试链上交易--参数校验失败")
    public void testOnChainAdjust_ValidateFailed() throws DcepException {
        OnChainAdjustReqDTO reqDTO = new OnChainAdjustReqDTO();
        Response<OnChainAdjustRespDTO> response = chainService.onChainAdjust(reqDTO);
        assertFalse(response.isSuccess());
        assertEquals(com.dcep.common.enums.ErrorEnum.VALIDATION_ERROR.getCode(), response.getErrorCode());
    }

    @Test
    @DisplayName("测试链上交易--业务信息校验失败")
    public void testOnChainAdjust_BusinessCheckFailed() throws DcepException, IllegalAccessException {

        //系统清零中
        OnChainAdjustReqDTO reqDTO = createOnChainAdjustReqDTO();

//        OrgDTO orgDTO = new OrgDTO();
//        orgDTO.setOrgState("ST01");
//        when(cache.getFiInf(any())).thenReturn(orgDTO);
//        orgCache.set(null,cache);

        checkUtil.when(() -> CheckUtil.checkClrMsgIdAndCreDtTm(any(),any(),anyInt())).thenReturn(true);
        infoCacheUtil.when(() -> InfoCacheUtil.checkInstState(any())).thenReturn(true);
        when(commonManager.checkSystemDate(any())).thenReturn(true);

        SystemStatusDO systemStatusDO = new SystemStatusDO();
        systemStatusDO.setCurSysDt(DateUtil.format(new Date(),DatePattern.NORM_DATE_FORMAT));
        when(systemStatusDOMapper.selectByPrimaryKey(CommonConstant.SysCode.WHOLESALE)).thenReturn(systemStatusDO);
        ZerooutCtrlDO zerooutCtrlDO = new ZerooutCtrlDO();
        zerooutCtrlDO.setPrcSts(ZerooutPrcStsEnum.PROCESS.getCode());
        when(zerooutCtrlDOMapper.selecPrcSts(any(),any())).thenReturn(zerooutCtrlDO);

        Response<OnChainAdjustRespDTO> response = chainService.onChainAdjust(reqDTO);
        assertFalse(response.isSuccess());
        assertEquals("DCEPO3017", response.getErrorCode());


        //报文系统工作日校验未通过
        when(commonManager.checkSystemDate(any())).thenReturn(false);
        response = chainService.onChainAdjust(reqDTO);
        assertFalse(response.isSuccess());
        assertEquals("DCEPO1037", response.getErrorCode());

        //测试机构未登录
//        orgDTO.setOrgState("ST05");
//        when(cache.getFiInf(any())).thenReturn(orgDTO);
//        orgCache.set(null,cache);

//        infoCacheUtil.when(() -> InfoCacheUtil.checkInstState(any())).thenReturn(false);
//        response = chainService.onChainAdjust(reqDTO);
//        assertFalse(response.isSuccess());
//        assertEquals("DCEPO3018", response.getErrorCode());

        //测试日期不合法
        checkUtil.when(() -> CheckUtil.checkClrMsgIdAndCreDtTm(any(),any(),anyInt())).thenReturn(false);
        response = chainService.onChainAdjust(reqDTO);
        assertFalse(response.isSuccess());
        assertEquals("DCEPO1037", response.getErrorCode());

    }

    @Test
    @DisplayName("测试链上交易--数据重复")
    public void testOnChainAdjust_DuplicateData() throws DcepException {
        OnChainAdjustReqDTO reqDTO = createOnChainAdjustReqDTO();
        infoCacheUtil.when(() -> InfoCacheUtil.checkInstState(any())).thenReturn(true);
        when(commonManager.checkSystemDate(any())).thenReturn(true);
        ZerooutCtrlDO zerooutCtrlDO = new ZerooutCtrlDO();
        zerooutCtrlDO.setPrcSts(ZerooutPrcStsEnum.SUCCESS.getCode());
        when(zerooutCtrlDOMapper.selecPrcSts(any(),any())).thenReturn(zerooutCtrlDO);
        SystemStatusDO systemStatusDO = new SystemStatusDO();
        systemStatusDO.setCurSysDt(DateUtil.format(new Date(),DatePattern.NORM_DATE_FORMAT));
        when(systemStatusDOMapper.selectByPrimaryKey(CommonConstant.SysCode.WHOLESALE)).thenReturn(systemStatusDO);
        RpcContext.getContext().set(Constant.PREPARE_INSERT_STEP,Constant.INSERT_STEP_ONE);
        when(chainManager.record(any(OnChainAdjustReqDTO.class))).thenThrow(new DuplicateKeyException("duplicate"));



        checkUtil.when(() -> CheckUtil.idempotentMatch(any(),any())).thenReturn(true);
        checkUtil.when(() -> CheckUtil.checkClrMsgIdAndCreDtTm(any(),any(),anyInt())).thenReturn(true);
        when(accountingInstrMapper.selectByMsgIdPrepare(any())).thenReturn(new AccountingInstrDO());

        when(accountingManager.transfer(any(),anyString(),anyString())).thenReturn(new Response<>(false,null, ErrorEnum.UNKNOWN_EXCEPTION.getCode(), ErrorEnum.UNKNOWN_EXCEPTION.getDescription()));

        //重复，等待结算
        SettlementProdDO settlementProdDO = new SettlementProdDO();
        settlementProdDO.setBizSts(ClearingStatusEnum.WAIT_SETTLE.getCode());
        when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
        Response<OnChainAdjustRespDTO> response = chainService.onChainAdjust(reqDTO);
        assertFalse(response.isSuccess());
        assertEquals("DCEPS9999", response.getErrorCode());

        //重复，且已结算
        settlementProdDO.setBizSts(ClearingStatusEnum.SETTLED.getCode());
        when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
        response = chainService.onChainAdjust(reqDTO);
        assertTrue(response.isSuccess());
        assertEquals("PR10", response.getResult().getBizStatus());

        //结算产品表重复，且不相等
        checkUtil.when(() -> CheckUtil.idempotentMatch(any(),any())).thenReturn(false);
        checkUtil.when(() -> CheckUtil.checkClrMsgIdAndCreDtTm(any(),any(),anyInt())).thenReturn(true);
        DcepException dcepException = assertThrows(DcepException.class,()-> chainService.onChainAdjust(reqDTO) );
        assertEquals("DCEPO0006",dcepException.getCode());


        //记账表重复
        RpcContext.getContext().set(Constant.PREPARE_INSERT_STEP,Constant.INSERT_STEP_TWO);
        dcepException = assertThrows(DcepException.class,()-> chainService.onChainAdjust(reqDTO) );
        assertEquals("DCEPS5103",dcepException.getCode());

    }
//
//    @Test
//    @DisplayName("测试链上交易--调用钱包")
//    public void testOnChainAdjust_Transfer() throws DcepException {
//
//        OnChainAdjustReqDTO reqDTO = createOnChainAdjustReqDTO();
//        infoCacheUtil.when(() -> InfoCacheUtil.checkInstState(any())).thenReturn(true);
//        checkUtil.when(() -> CheckUtil.checkClrMsgIdAndCreDtTm(any(),any(),anyInt())).thenReturn(true);
//        when(commonManager.checkSystemDate(any())).thenReturn(true);
//
//        ZerooutCtrlDO zerooutCtrlDO = new ZerooutCtrlDO();
//        zerooutCtrlDO.setPrcSts(ZerooutPrcStsEnum.SUCCESS.getCode());
//        when(zerooutCtrlDOMapper.selecPrcSts(any(),any())).thenReturn(zerooutCtrlDO);
//
//        SystemStatusDO systemStatusDO = new SystemStatusDO();
//        systemStatusDO.setCurSysDt(DateUtil.format(new Date(),DatePattern.NORM_DATE_FORMAT));
//        when(systemStatusDOMapper.selectByPrimaryKey(CommonConstant.SysCode.WHOLESALE)).thenReturn(systemStatusDO);
//
//
//        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
//        when(chainManager.record(reqDTO)).thenReturn(new Response<>(accountingInstrDO));
//
//        //完成结算
//        TransferRespDTO transferRespDTO = new TransferRespDTO();
//        when(accountingManager.transfer(any(), any(), any())).thenReturn(new Response<>(transferRespDTO));
//        OnChainAdjustRespDTO onChainAdjustRespDTO = new OnChainAdjustRespDTO();
//        onChainAdjustRespDTO.setBizStatus("PR10");
//        when(chainManager.settleComplete(any(), any())).thenReturn(new Response<>(onChainAdjustRespDTO));
//        Response<OnChainAdjustRespDTO> response = chainService.onChainAdjust(reqDTO);
//        assertTrue(response.isSuccess());
//        assertEquals("PR10", response.getResult().getBizStatus());
//
//
//
//        //结算失败
//        when(accountingManager.transfer(any(), any(), any())).thenReturn(new Response<>(false,null, ErrorEnum.BUSI_COMP_ERROR.getCode(), ErrorEnum.BUSI_COMP_ERROR.getDescription()));
//        onChainAdjustRespDTO = new OnChainAdjustRespDTO();
//        onChainAdjustRespDTO.setBizStatus(ClearingStatusEnum.FAILED.getCode());
//        when(chainManager.settleFail(any(), any(),any())).thenReturn(new Response<>(onChainAdjustRespDTO));
//        response = chainService.onChainAdjust(reqDTO);
//        assertTrue(response.isSuccess());
//        assertEquals("PR01", response.getResult().getBizStatus());
//
//        //结算调用异常
//        when(accountingManager.transfer(any(), any(), any())).thenReturn(new Response<>(false,null, ErrorEnum.UNKNOWN_EXCEPTION.getCode(), ErrorEnum.UNKNOWN_EXCEPTION.getDescription()));
//        when(chainManager.settleComplete(transferRespDTO, accountingInstrDO)).thenReturn(new Response<>(false,null, ErrorEnum.UNKNOWN_EXCEPTION.getCode(), ErrorEnum.UNKNOWN_EXCEPTION.getDescription()));
//        response = chainService.onChainAdjust(reqDTO);
//        assertFalse(response.isSuccess());
//        assertEquals(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), response.getErrorCode());
//
//    }

//
//
//
//    @Test
//    @DisplayName("测试链上交易补偿逻辑--空参数")
//    public void testRedo_nullParameters() {
//        chainService.redo(null, null, null);
//        verifyNoInteractions(settlementManager);
//        verifyNoInteractions(commonRecordMapper);
//    }
//
//    @Test
//    @DisplayName("测试链上交易补偿逻辑--交易控制超时")
//    public void testRedo_timeout() {
//        CommonStsctrlDO stsctrlDO = new CommonStsctrlDO();
//        stsctrlDO.setConfirmTimeout(new Date(System.currentTimeMillis() - 10000));
//        stsctrlDO.setPresumeStatus(ClearingStatusEnum.PRESUME_FAILED.getCode());
//
//        CommonRecordDO commonRecordDO = new CommonRecordDO();
//        OnChainAdjustReqDTO onChainAdjustReqDTO = new OnChainAdjustReqDTO();
//        onChainAdjustReqDTO.setUseCurrentSystemFlag("A");
//        commonRecordDO.setDocument(JSON.toJSONString(onChainAdjustReqDTO) );
//        when(commonRecordMapper.selectByPrimaryKey(any())).thenReturn(commonRecordDO);
//
//        when(accountingManager.transfer(any(),any(),any())).thenReturn(new Response<>(false,null, ErrorEnum.BUSI_DB_ACC_ERR.getCode(), ErrorEnum.BUSI_DB_ACC_ERR.getDescription()));
//
//
//        SettlementProdDO settle = new SettlementProdDO();
//        settle.setBizSts(ClearingStatusEnum.WAIT_SETTLE.getCode());
//        settle.setMsgId("20251015203120347452823150745000");
//        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
//        chainService.redo(stsctrlDO, settle, accountingInstrDO);
//
//        verify(chainManager).settleFail( any(), any(), any());
////        verifyNoInteractions(commonRecordMapper);
//    }
//
//    @Test
//    @DisplayName("测试链上交易补偿逻辑")
//    public void testRedo_notTimeout() {
//        CommonStsctrlDO stsctrlDO = new CommonStsctrlDO();
//        stsctrlDO.setConfirmTimeout(new Date(System.currentTimeMillis() + 10000));
//
//        SettlementProdDO settle = new SettlementProdDO();
//        settle.setBizSts(ClearingStatusEnum.WAIT_SETTLE.getCode());
//        settle.setMsgId("20251015203120347452823150745000");
//        CommonRecordDO commonRecordDO = new CommonRecordDO();
//        commonRecordDO.setDocument("{\"useCurrentSystemFlag\":\"A\"}");
//        when(commonRecordMapper.selectByPrimaryKey(any(CommonRecordDO.class))).thenReturn(commonRecordDO);
//
//        when(accountingManager.transfer(any(),any(),any())).thenReturn(new Response<>(false,null, ErrorEnum.UNKNOWN_EXCEPTION.getCode(), ErrorEnum.UNKNOWN_EXCEPTION.getDescription()));
//
//        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
//        chainService.redo(stsctrlDO, settle, accountingInstrDO);
//
//        verify(settlementManager, never()).fail(any(), any(),  anyBoolean());
//        verify(commonRecordMapper).selectByPrimaryKey(any(CommonRecordDO.class));
//        verify(accountingManager).transfer(any(), any(),any());
//
//    }
//
//    @Test
//    @DisplayName("测试链上交易补偿逻辑--未结算状态")
//    public void testRedo_notWaitSettle() {
//
//        CommonStsctrlDO stsctrlDO = new CommonStsctrlDO();
//        stsctrlDO.setConfirmTimeout(new Date(System.currentTimeMillis() + 10000));
//
//        SettlementProdDO settle = new SettlementProdDO();
//        settle.setBizSts(ClearingStatusEnum.PRESUME_FAILED.getCode());
//        settle.setMsgId("20251015203120347452823150745000");
//        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
//        chainService.redo(stsctrlDO, settle, accountingInstrDO);
//
//        verifyNoInteractions(settlementManager);
//        verifyNoInteractions(commonRecordMapper);
//    }
//
//    @Test
//    @DisplayName("测试链上交易补偿逻辑--档案为空")
//    public void testRedo_commonRecordNull() {
//        CommonStsctrlDO stsctrlDO = new CommonStsctrlDO();
//        stsctrlDO.setConfirmTimeout(new Date(System.currentTimeMillis() + 10000));
//        SettlementProdDO settle = new SettlementProdDO();
//        settle.setBizSts(ClearingStatusEnum.WAIT_SETTLE.getCode());
//        settle.setMsgId("20251015203120347452823150745000");
//        when(commonRecordMapper.selectByPrimaryKey(any(CommonRecordDO.class))).thenReturn(null);
//        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
//        chainService.redo(stsctrlDO, settle, accountingInstrDO);
//
//        verifyNoInteractions(settlementManager);
//        verify(commonRecordMapper).selectByPrimaryKey(any(CommonRecordDO.class));
//    }



}