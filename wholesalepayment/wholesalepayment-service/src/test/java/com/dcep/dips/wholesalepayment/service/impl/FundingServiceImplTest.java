package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.dips.acctrans.constants.Constant.AccountingStatus;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustRespDTO;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.dal.bo.ActgAdjustRespBO;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.FundAdjustProdMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.HvpsTransMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dto.BizStatusDTO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.dc181.Dcep18100101DTO;
import com.dcep.dips.wholesalepayment.dto.funding.IncreaseReqDTO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.ClearingCenterManager;
import com.dcep.dips.wholesalepayment.manager.FundingManager;
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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FundingServiceImplTest {

    @InjectMocks
    private FundingServiceImpl fundingServiceImpl;

    @Mock
    private HvpsTransMapper hvpsTransMapper;

    @Mock
    private FundAdjustProdMapper fundAdjustProdMapper;

    @Mock
    private AccountingInstrMapper accountingInstrMapper;

    @Mock
    private FundingManager fundingManager;

    @Mock
    private AccountingManager accountingManager;

    @Mock
    private ClearingCenterManager clearingCenterManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(fundingServiceImpl, "CREDTTM_INTERVAL", Long.toString(10 * 60 * 1000));
        ReflectionTestUtils.setField(fundingServiceImpl, "MSGID_DATE_INTERVAL", "1");
    }

    @Test
    @DisplayName("测试预注资调增成功")
    public void testPreIncreaseSuccess() throws DcepException {
        IncreaseReqDTO increaseReqDTO = new IncreaseReqDTO();
        increaseReqDTO.setMsgTp(MsgTpEnum.CDT_FUND_PRE_INCREASE.getCode());
        increaseReqDTO.setClearingMemberId("123456789012");
        increaseReqDTO.setMsgId("1234567890123456");
        increaseReqDTO.setClearingSystemId("HVPS");
        increaseReqDTO.setEndToEndId("1234567890123456");
        increaseReqDTO.setReceiveMemberId("123456789012");
        increaseReqDTO.setCurrency("CNY");
        increaseReqDTO.setAmount(new BigDecimal("0.00"));

        Response<AccountingInstrDO> response = new Response<>();
        response.setResult(new AccountingInstrDO(IdUtils.randomTransIdWithBizDt("20250101")));
        when(fundingManager.increaseRecord(any(IncreaseReqDTO.class))).thenReturn(response);

        Response<BizStatusDTO> result = fundingServiceImpl.increase(increaseReqDTO);
        assertEquals(ClearingStatusEnum.SETTLED.getCode(), result.getResult().getBizSts());
    }

    @Test
    @DisplayName("测试注资调增成功")
    public void testIncreaseSuccess() throws DcepException {
        IncreaseReqDTO increaseReqDTO = new IncreaseReqDTO();
        increaseReqDTO.setMsgTp(MsgTpEnum.CDT_FUND_INCREASE.getCode());
        increaseReqDTO.setClearingMemberId("123456789012");
        increaseReqDTO.setMsgId("1234567890123456");
        increaseReqDTO.setClearingSystemId("HVPS");
        increaseReqDTO.setEndToEndId("1234567890123456");
        increaseReqDTO.setReceiveMemberId("123456789012");
        increaseReqDTO.setCurrency("CNY");
        increaseReqDTO.setAmount(new BigDecimal("0.00"));

        Response<AccountingInstrDO> response = new Response<>();
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setMsgId("1234567890123456");
        accountingInstrDO.setActgSts(AccountingStatus.SUCCESS);
        response.setResult(accountingInstrDO);
        when(fundingManager.increaseRecord(any(IncreaseReqDTO.class))).thenReturn(response);

        Response<BizStatusDTO> result = fundingServiceImpl.increase(increaseReqDTO);
        assertEquals(ClearingStatusEnum.SETTLED.getCode(), result.getResult().getBizSts());
    }

    @Test
    @DisplayName("测试注资调增失败")
    public void testIncreaseFailed() throws DcepException {
        IncreaseReqDTO increaseReqDTO = new IncreaseReqDTO();
        increaseReqDTO.setMsgTp(MsgTpEnum.CDT_FUND_INCREASE.getCode());
        increaseReqDTO.setClearingMemberId("123456789012");
        increaseReqDTO.setMsgId("1234567890123456");
        increaseReqDTO.setClearingSystemId("HVPS");
        increaseReqDTO.setEndToEndId("1234567890123456");
        increaseReqDTO.setReceiveMemberId("123456789012");
        increaseReqDTO.setCurrency("CNY");
        increaseReqDTO.setAmount(new BigDecimal("0.00"));

        Response<AccountingInstrDO> response = new Response<>();
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setMsgId("789012");
        accountingInstrDO.setActgSts(AccountingStatus.FAILED);
        response.setResult(accountingInstrDO);
        when(fundingManager.increaseRecord(any(IncreaseReqDTO.class))).thenReturn(response);

        Response<BizStatusDTO> result = fundingServiceImpl.increase(increaseReqDTO);
        assertEquals(ClearingStatusEnum.FAILED.getCode(), result.getResult().getBizSts());
    }

    @Test
    @DisplayName("测试结算钱包调用失败")
    public void testAccountingManagerFailed() throws DcepException {
        IncreaseReqDTO increaseReqDTO = new IncreaseReqDTO();
        increaseReqDTO.setMsgTp(MsgTpEnum.CDT_FUND_INCREASE.getCode());
        increaseReqDTO.setClearingMemberId("123456789012");
        increaseReqDTO.setMsgId("1234567890123456");
        increaseReqDTO.setClearingSystemId("HVPS");
        increaseReqDTO.setEndToEndId("1234567890123456");
        increaseReqDTO.setReceiveMemberId("123456789012");
        increaseReqDTO.setCurrency("CNY");
        increaseReqDTO.setAmount(new BigDecimal("0.00"));

        Response<AccountingInstrDO> response = new Response<>();
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setMsgId("1234567890123456");
        accountingInstrDO.setActgSts(AccountingStatus.PROCESSING);
        response.setResult(accountingInstrDO);
        when(fundingManager.increaseRecord(any(IncreaseReqDTO.class))).thenReturn(response);

        Response<AdjustRespDTO> actgResp = new Response<>();
        when(accountingManager.adjust(any(AccountingInstrDO.class), any())).thenReturn(actgResp);

        try {
            fundingServiceImpl.increase(increaseReqDTO);
            assertTrue(false);
        } catch (DcepException e) {
            assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(), e.getCode());
        }
    }

    @Test
    @DisplayName("测试更新状态失败")
    public void testUpdateStatusFailed() throws DcepException {
        IncreaseReqDTO increaseReqDTO = new IncreaseReqDTO();
        increaseReqDTO.setMsgTp(MsgTpEnum.CDT_FUND_INCREASE.getCode());
        increaseReqDTO.setClearingMemberId("123456789012");
        increaseReqDTO.setMsgId("1234567890123456");
        increaseReqDTO.setClearingSystemId("HVPS");
        increaseReqDTO.setEndToEndId("1234567890123456");
        increaseReqDTO.setReceiveMemberId("123456789012");
        increaseReqDTO.setCurrency("CNY");
        increaseReqDTO.setAmount(new BigDecimal("0.00"));

        Response<AccountingInstrDO> response = new Response<>();
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setMsgId("789012");
        accountingInstrDO.setActgSts(AccountingStatus.PROCESSING);
        response.setResult(accountingInstrDO);
        when(fundingManager.increaseRecord(any(IncreaseReqDTO.class))).thenReturn(response);

        Response<AdjustRespDTO> actgResp = new Response<>();
        AdjustRespDTO adjustRespDTO = new AdjustRespDTO();
        adjustRespDTO.setBizCode("123456");
        adjustRespDTO.setBizMsg("失败原因");
        actgResp.setResult(adjustRespDTO);
        when(accountingManager.adjust(any(AccountingInstrDO.class), any())).thenReturn(actgResp);

        List<EnvelopeDTO<GwDTO>> dtoList = Collections.emptyList();
        when(fundingManager.updateIncreaseFinishStatus(any(IncreaseReqDTO.class), any(AccountingInstrDO.class), any(ActgAdjustRespBO.class))).thenReturn(dtoList);

        Response<BizStatusDTO> result = fundingServiceImpl.increase(increaseReqDTO);
        assertEquals(ClearingStatusEnum.FAILED.getCode(), result.getResult().getBizSts());
    }
}
