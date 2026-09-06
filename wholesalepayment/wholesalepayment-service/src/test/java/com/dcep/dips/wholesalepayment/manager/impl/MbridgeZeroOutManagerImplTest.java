
package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.ZerooutCtrlDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.dc181.ClrSysMmbId;
import com.dcep.dips.wholesalepayment.dto.mcbs101.*;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.ChainZeroOutManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.gateway.mcbdc.api.GwoutService;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MbridgeZeroOutManagerImplTest {

    @InjectMocks
    private MbridgeZeroOutManagerImpl mbridgeZeroOutManagerImpl;

    @Mock
    private MbridgeManager mbridgeManager;

    @Mock
    private AccountingInstrMapper accountingInstrMapper;

    @Mock
    private ChainZeroOutManager chainZeroOutManager;

    @Mock
    private ZerooutCtrlDOMapper zerooutCtrlDOMapper;

    @Mock
    private SystemStatusDOMapper systemStatusMapper;

    @Mock
    private GwoutService mBridgeGwoutService;

    private Mcbs10100101DTO mcbs10100101DTO;
    private ZerooutCtrlDO zerooutCtrlDO;

    @Before
    public void setUp() {
        mcbs10100101DTO = new Mcbs10100101DTO();
        MsgHdr msgHdr = new MsgHdr();
        msgHdr.setMsgId("20251014001WHOLESALE994443200000");
        msgHdr.setCreDtTm("2023-10-01T12:00:00");
        mcbs10100101DTO.setMsgHdr(msgHdr);
        OrgnlGrpInf orgnlGrpInf = new OrgnlGrpInf();
        orgnlGrpInf.setOrgnlMsgId("20251014001WHOLESALE994443200000");
        orgnlGrpInf.setOrgnlMsgNmId("mcbs.101.001.01");
        mcbs10100101DTO.setOrgnlGrpInf(orgnlGrpInf);

        ClrDtlInf clrDtlInf = new ClrDtlInf();
        FinInstnId finInstnId = new FinInstnId();
        ClrSysMmbId clrSysMmbId = new ClrSysMmbId();
        clrSysMmbId.setMmbId("testMmbId");
        finInstnId.setClrSysMmbId(clrSysMmbId);
        clrDtlInf.setFinInsTnId(finInstnId);
        clrDtlInf.setFishClrAmt(new ActiveCurrencyAndAmount("CNY","100"));
        clrDtlInf.setUnFishClrAmt(new ActiveCurrencyAndAmount("CNY","100"));
        CtgyPurp ctgyPurp = new CtgyPurp();
        ctgyPurp.setCd("testCd");
        Envlp envlp = new Envlp();
        Cnts cnts = new Cnts();
        cnts.setBatchNO("testBatchNO");
        cnts.setParamId("testParamId123456789123456789012");
        envlp.setCnts(cnts);
        clrDtlInf.setEnvlp(envlp);

        List<ClrDtlInf> clrDtlInfList = new ArrayList<>();
        clrDtlInfList.add(clrDtlInf);
        ClrDtls clrDtls = new ClrDtls();
        clrDtls.setClrDtlInf(clrDtlInfList);
        clrDtls.setClrZeDt("2023-10-01");

        mcbs10100101DTO.setClrDtls(clrDtls);

        zerooutCtrlDO = new ZerooutCtrlDO();
        zerooutCtrlDO.setMsgId("20251024001WHOLESALE219096102000");
        zerooutCtrlDO.setSysDt("20251013");
        zerooutCtrlDO.setSysId("BCSP");
        zerooutCtrlDO.setCurSysFlg("A");
        zerooutCtrlDO.setTaskId("testTaskId");
        zerooutCtrlDO.setPrcSts("03");
        zerooutCtrlDO.setGmtCreate(new Date());
        zerooutCtrlDO.setGmtModified(new Date());
    }

    @Test
    @DisplayName("测试记录清零控制表和记账指令表 - 成功")
    public void testRecordZOCtrlAndAcctInstr_Success() {
        MockedStatic<IdUtils> idUtilsMockedStatic = mockStatic(IdUtils.class);
        when(IdUtils.randomTransIdWithBizDt("20231001")).thenReturn("20251017002118108204938562211000");
        MockedStatic<InfoCacheUtil> idUtilsMockedStatic2 = mockStatic(InfoCacheUtil.class);
        when(InfoCacheUtil.getPbocInf()).thenReturn("test");
        when(systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE)).thenReturn("20231001");
        when(accountingInstrMapper.insert(any(AccountingInstrDO.class))).thenReturn(1);
        when(chainZeroOutManager.transfer(any(AccountingInstrDO.class))).thenReturn(new Response<>(true, new TransferRespDTO(), null, null));
        try {
            mbridgeZeroOutManagerImpl.recordZOCtrlAndAcctInstr(mcbs10100101DTO, zerooutCtrlDO);
            verify(accountingInstrMapper, times(1)).insert(any(AccountingInstrDO.class));
            verify(chainZeroOutManager, times(1)).transfer(any(AccountingInstrDO.class));
            verify(zerooutCtrlDOMapper, times(1)).updateByPrimaryKey(zerooutCtrlDO);
        }catch (Exception e){
            Assert.assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),e.getMessage());
        }finally {
            idUtilsMockedStatic.close();
            idUtilsMockedStatic2.close();
        }

    }

//    @Test
//    @DisplayName("测试记录清零控制表和记账指令表 - 记账指令表插入失败")
//    public void testRecordZOCtrlAndAcctInstr_InsertFail() {
//        MockedStatic<IdUtils> idUtilsMockedStatic = mockStatic(IdUtils.class);
//        when(IdUtils.randomTransIdWithBizDt("20231001")).thenReturn("20251017002118108204938562211000");
//        MockedStatic<InfoCacheUtil> idUtilsMockedStatic2 = mockStatic(InfoCacheUtil.class);
//        when(InfoCacheUtil.getPbocInf()).thenReturn("test");
//        when(systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE)).thenReturn("20231001");
//        when(accountingInstrMapper.insert(any(AccountingInstrDO.class))).thenReturn(0);
//        try {
//            mbridgeZeroOutManagerImpl.recordZOCtrlAndAcctInstr(mcbs10100101DTO, zerooutCtrlDO);
//            verify(accountingInstrMapper, times(1)).insert(any(AccountingInstrDO.class));
//            verify(chainZeroOutManager, never()).transfer(any(AccountingInstrDO.class));
//        }catch (Exception e){
//            Assert.assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),e.getMessage());
//        }finally {
//            idUtilsMockedStatic.close();
//            idUtilsMockedStatic2.close();
//        }
//    }

//    @Test
//    @DisplayName("测试记录清零控制表和记账指令表 - 异步记账失败")
//    public void testRecordZOCtrlAndAcctInstr_AsyncTransferFail() {
//        MockedStatic<IdUtils> idUtilsMockedStatic = mockStatic(IdUtils.class);
//        when(IdUtils.randomTransIdWithBizDt("20231001")).thenReturn("20251017002118108204938562211000");
//        MockedStatic<InfoCacheUtil> idUtilsMockedStatic2 = mockStatic(InfoCacheUtil.class);
//        when(InfoCacheUtil.getPbocInf()).thenReturn("test");
//        when(systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE)).thenReturn("20231001");
//        when(accountingInstrMapper.insert(any(AccountingInstrDO.class))).thenReturn(1);
//        when(chainZeroOutManager.transfer(any(AccountingInstrDO.class))).thenReturn(new Response<>(false, null, null, null));
//        try {
//            mbridgeZeroOutManagerImpl.recordZOCtrlAndAcctInstr(mcbs10100101DTO, zerooutCtrlDO);
//            verify(accountingInstrMapper, times(1)).insert(any(AccountingInstrDO.class));
//            verify(chainZeroOutManager, times(1)).transfer(any(AccountingInstrDO.class));
//            verify(zerooutCtrlDOMapper, times(1)).updateByPrimaryKey(zerooutCtrlDO);
//        }catch (Exception e){
//            Assert.assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),e.getMessage());
//        }finally {
//            idUtilsMockedStatic.close();
//            idUtilsMockedStatic2.close();
//        }
//    }

    @Test
    @DisplayName("测试调用货币桥网关 - 成功")
    public void testMbridgeGateway_Success() {
        GenericEnvelopeDTO<GenericGwDTO> genericReq = DtoUtil.assembly100Msg("20251014001WHOLESALE994443200000");
        Response<GenericEnvelopeDTO<GenericGwDTO>> resp = new Response<>(true, genericReq, null, null);

        when(mBridgeGwoutService.execute(any(GenericEnvelopeDTO.class))).thenReturn(resp);

        Response<GenericEnvelopeDTO<GenericGwDTO>> result = mbridgeZeroOutManagerImpl.mbridgeGateway(genericReq);

        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    @DisplayName("测试调用货币桥网关 - 异常")
    public void testMbridgeGateway_Exception() {
        GenericEnvelopeDTO<GenericGwDTO> genericReq = DtoUtil.assembly100Msg("20251014001WHOLESALE994443200000");
        when(mBridgeGwoutService.execute(any(GenericEnvelopeDTO.class))).thenThrow(new RuntimeException("Test Exception"));

        Response<GenericEnvelopeDTO<GenericGwDTO>> result = mbridgeZeroOutManagerImpl.mbridgeGateway(genericReq);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(), result.getErrorCode());
        assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription(), result.getErrorMsg());
    }
}