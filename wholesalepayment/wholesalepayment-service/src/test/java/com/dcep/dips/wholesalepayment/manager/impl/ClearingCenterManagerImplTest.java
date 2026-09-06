package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.clearingcenter.api.HvpsService;
import com.dcep.clearingcenter.dto.settlement.HvpsReqDTO;
import com.dcep.clearingcenter.dto.settlement.HvpsRspDTO;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.HvpsTransMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.StorageForwardMapper;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.dc181.Dcep18100101DTO;
import com.dcep.dips.wholesalepayment.enums.HvpsAdjTypEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.manager.ClearingCenterManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.dips.wholesalepayment.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class ClearingCenterManagerImplTest {

    @InjectMocks
    private ClearingCenterManagerImpl clearingCenterManagerImpl;

    @Mock
    private HvpsService hvpsService;

    @Mock
    private ExecutorService asyncBizPool;

    @Mock
    private HvpsTransMapper hvpsTransMapper;

    @Mock
    private StorageForwardMapper storageForwardMapper;

    @Mock
    private ClearingCenterManager clearingCenterManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // Mock静态方法
        mockStatic(CommonUtil.class);
        mockStatic(InfoCacheUtil.class);
    }

    @Test
    @DisplayName("测试发送大额系统成功")
    public void testSendHvpsSuccess() throws DcepException {
        EnvelopeDTO<GwDTO> envelopeDTO = loadEnvelopeDTO_181();
        Dcep18100101DTO dcep181DTO = (Dcep18100101DTO)envelopeDTO.getSoapBody().getT();

        HvpsReqDTO hvpsReqDTO = new HvpsReqDTO();
        hvpsReqDTO.setMsgTp(dcep181DTO.hvpsMsgTp());
        hvpsReqDTO.setEndToEndId(dcep181DTO.msgId());
        hvpsReqDTO.setBizTp(dcep181DTO.adjustTp());
        hvpsReqDTO.setDbtrClearingMemberId("123456789012");
        hvpsReqDTO.setDbtrBrnchId("123456789012");
        hvpsReqDTO.setCdtrClearingMemberId("123456789012");
        hvpsReqDTO.setCdtrBranchId("123456789012");
        hvpsReqDTO.setCurrency(dcep181DTO.currency());
        hvpsReqDTO.setAmount(new BigDecimal(dcep181DTO.adjustAmt()));

        HvpsRspDTO hvpsRspDTO = new HvpsRspDTO();
        Response<HvpsRspDTO> hvpsResp = new Response<>();
        hvpsResp.setResult(hvpsRspDTO);

        when(hvpsService.sendHvpsMessage(any(HvpsReqDTO.class))).thenReturn(hvpsResp);

        clearingCenterManagerImpl.sendHvps(envelopeDTO, "DEV");

        verify(hvpsService, times(1)).sendHvpsMessage(any(HvpsReqDTO.class));
        verify(clearingCenterManager, times(1)).delStorageForwardAndInsertHvpsTrans(anyString(), anyString(), any(HvpsReqDTO.class), any(HvpsRspDTO.class));
    }

    @Test
    @DisplayName("测试发送大额系统失败")
    public void testSendHvpsFailure() throws DcepException {
        EnvelopeDTO<GwDTO> envelopeDTO = loadEnvelopeDTO_181();
        Dcep18100101DTO dcep181DTO = (Dcep18100101DTO)envelopeDTO.getSoapBody().getT();

        HvpsReqDTO hvpsReqDTO = new HvpsReqDTO();
        hvpsReqDTO.setMsgTp(dcep181DTO.hvpsMsgTp());
        hvpsReqDTO.setEndToEndId(dcep181DTO.msgId());
        hvpsReqDTO.setBizTp(dcep181DTO.adjustTp());
        hvpsReqDTO.setDbtrClearingMemberId("123456789012");
        hvpsReqDTO.setDbtrBrnchId("123456789012");
        hvpsReqDTO.setCdtrClearingMemberId("123456789012");
        hvpsReqDTO.setCdtrBranchId("123456789012");
        hvpsReqDTO.setCurrency(dcep181DTO.currency());
        hvpsReqDTO.setAmount(new BigDecimal(dcep181DTO.adjustAmt()));

        Response<HvpsRspDTO> hvpsResp = new Response<>(false, new HvpsRspDTO());

        when(hvpsService.sendHvpsMessage(any(HvpsReqDTO.class))).thenReturn(hvpsResp);

        clearingCenterManagerImpl.sendHvps(envelopeDTO, "DEV");

        verify(hvpsService, times(1)).sendHvpsMessage(any(HvpsReqDTO.class));
    }

    @Test
    @DisplayName("测试发送大额系统（结算钱包）成功")
    public void testSendHvpsZeroOutSuccess() throws DcepException {
        ZeroOutReqDTO zeroOutReqDTO = new ZeroOutReqDTO();
        zeroOutReqDTO.setTransId("01234567890123456789012345678912");
        zeroOutReqDTO.setAccountingDate("20250101");
        zeroOutReqDTO.setCurrency("CNY");
        zeroOutReqDTO.setAmount(new BigDecimal("1000"));
        zeroOutReqDTO.setDbtrClearingMemberId("1234567890");
        zeroOutReqDTO.setMemberId("1234567890");
        zeroOutReqDTO.setCdtrClearingMemberId("1234567890");

        FundAdjustProdDO fundAdjustProdDO = new FundAdjustProdDO();
        fundAdjustProdDO.setMsgId("1234567890123456");
        fundAdjustProdDO.setMsgTp(MsgTpEnum.CDT_FUND_DECREASE_OUT.getCode());
        fundAdjustProdDO.setAdjustAmt(new BigDecimal("1000"));
        fundAdjustProdDO.setCurrency("CNY");
        fundAdjustProdDO.setAdjustAmt(new BigDecimal("1000"));

        HvpsReqDTO hvpsReqDTO = new HvpsReqDTO();
        hvpsReqDTO.setMsgTp(MsgTpEnum.CDT_FUND_DECREASE_OUT.getCode());
        hvpsReqDTO.setEndToEndId(fundAdjustProdDO.getMsgId());
        hvpsReqDTO.setBizTp(HvpsAdjTypEnum.ZERO_OUT.getCode());
        hvpsReqDTO.setDbtrClearingMemberId("123456789012");
        hvpsReqDTO.setDbtrBrnchId("123456789012");
        hvpsReqDTO.setCdtrClearingMemberId("123456789012");
        hvpsReqDTO.setCdtrBranchId("123456789012");
        hvpsReqDTO.setCurrency("CNY");
        hvpsReqDTO.setAmount(new BigDecimal("10.00"));

        HvpsRspDTO hvpsRspDTO = new HvpsRspDTO();
        Response<HvpsRspDTO> hvpsResp = new Response<>();
        hvpsResp.setResult(hvpsRspDTO);

        when(hvpsService.sendHvpsMessage(any(HvpsReqDTO.class))).thenReturn(hvpsResp);

        clearingCenterManagerImpl.sendHvps(zeroOutReqDTO, fundAdjustProdDO, "DEV");

        verify(hvpsService, times(1)).sendHvpsMessage(any(HvpsReqDTO.class));
        verify(clearingCenterManager, times(1)).delStorageForwardAndInsertHvpsTrans(anyString(), anyString(), any(HvpsReqDTO.class), any(HvpsRspDTO.class));
    }

    @Test
    @DisplayName("测试发送大额系统（结算钱包）失败")
    public void testSendHvpsZeroOutFailure() throws DcepException {
        ZeroOutReqDTO zeroOutReqDTO = new ZeroOutReqDTO();
        zeroOutReqDTO.setTransId("01234567890123456789012345678912");
        zeroOutReqDTO.setAccountingDate("20250101");
        zeroOutReqDTO.setCurrency("CNY");
        zeroOutReqDTO.setAmount(new BigDecimal("1000"));
        zeroOutReqDTO.setDbtrClearingMemberId("1234567890");
        zeroOutReqDTO.setMemberId("1234567890");
        zeroOutReqDTO.setCdtrClearingMemberId("1234567890");

        FundAdjustProdDO fundAdjustProdDO = new FundAdjustProdDO();
        fundAdjustProdDO.setMsgId("1234567890123456");
        fundAdjustProdDO.setMsgTp(MsgTpEnum.CDT_FUND_DECREASE_OUT.getCode());
        fundAdjustProdDO.setAdjustAmt(new BigDecimal("1000"));
        fundAdjustProdDO.setCurrency("CNY");
        fundAdjustProdDO.setAdjustAmt(new BigDecimal("1000"));

        HvpsReqDTO hvpsReqDTO = new HvpsReqDTO();
        hvpsReqDTO.setMsgTp(MsgTpEnum.CDT_FUND_DECREASE_OUT.getCode());
        hvpsReqDTO.setEndToEndId(fundAdjustProdDO.getMsgId());
        hvpsReqDTO.setBizTp(HvpsAdjTypEnum.ZERO_OUT.getCode());
        hvpsReqDTO.setDbtrClearingMemberId("123456789012");
        hvpsReqDTO.setDbtrBrnchId("123456789012");
        hvpsReqDTO.setCdtrClearingMemberId("123456789012");
        hvpsReqDTO.setCdtrBranchId("123456789012");
        hvpsReqDTO.setCurrency("CNY");
        hvpsReqDTO.setAmount(new BigDecimal("10.00"));

        Response<HvpsRspDTO> hvpsResp = new Response<>(false, new HvpsRspDTO());

        when(hvpsService.sendHvpsMessage(hvpsReqDTO)).thenReturn(hvpsResp);

        clearingCenterManagerImpl.sendHvps(zeroOutReqDTO, fundAdjustProdDO, "DEV");

        verify(hvpsService, times(1)).sendHvpsMessage(any(HvpsReqDTO.class));
    }

    public EnvelopeDTO<GwDTO> loadEnvelopeDTO_181() {
        String msgTp = "dcep.181.001.01";
        String shortSender = "002";
        String shortReceiver = "003";
        String msgId = TestUtils.getMsgId(shortSender, msgTp);
        String creDtTm = TestUtils.getCurCreDtTm();
        String xmlFile = "funding/181.xml";

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader soapHeader = TestUtils.createSoapHeader(msgTp, msgId, creDtTm, shortSender, shortReceiver);
        envelopeDTO.setSoapHeader(soapHeader);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{Sender}}", TestUtils.getFullOrgId(shortSender));

        Dcep18100101DTO dcep181DTO = TestUtils.createDcepDTO(xmlFile, dataMap, Dcep18100101DTO.class);
        envelopeDTO.setSoapBody(new SoapBody<>(dcep181DTO));
        return envelopeDTO;
    }
}
