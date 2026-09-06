package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep90000101DTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.dips.acctrans.constants.Constant;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.common.util.DateUtils;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.FundAdjustProdMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.HvpsTransMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dal.model.HvpsTransDO;
import com.dcep.dips.wholesalepayment.dto.BizStatusDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutRespDTO;
import com.dcep.dips.wholesalepayment.dto.dc181.Dcep18100101DTO;
import com.dcep.dips.wholesalepayment.dto.dc183.Dcep18300101DTO;
import com.dcep.dips.wholesalepayment.dto.funding.IncreaseReqDTO;
import com.dcep.dips.wholesalepayment.dto.hvps.ClearReportReqDTO;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.dips.wholesalepayment.utils.TestUtils;
import com.dcep.dips.wholesalepayment.utils.TimeUtils;
import com.dcep.infocache.api.dto.OwnershipParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class FundingServiceIntegrationTest {

    @Autowired
    private FundingServiceImpl fundingService;

    @Autowired
    private SystemStatusDOMapper systemStatusMapper;

    @Autowired
    private HvpsTransMapper hvpsTransMapper;

    @Autowired
    private AccountingInstrMapper accountingInstrMapper;

    @Autowired
    private FundAdjustProdMapper fundAdjustProdMapper;

    @Before
    public void setUp() {
    }

    /**
     */
    @Test
    @DisplayName("获取获取信息")
    public void testInfoCacheUtil() throws DcepException {
        OwnershipParam publicParam = InfoCacheUtil.getPublicParam(CommonConstant.WLLT_ID_ASSETS_ACS);
        System.out.println(publicParam);
    }

    /**
     * 注资调减
     *   PR12-清算排队
     *   PR09-已拒绝
     *   PR18-已退回
     *   PR04-已清算
     */
    @Test
    @DisplayName("注资调减-PR12-清算排队")
    public void testDecrease_HVPS_PR12() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO("dcep.181.001.01");

        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            Response<EnvelopeDTO<GwDTO>> response = fundingService.decrease(gwReqDTO);
            receiveHvpsNotify(response, "PR12", "HVPS0001", "运营中心在HVPS头寸不足，已清算队列");
        }
    }

    @Test
    @DisplayName("注资调减-PR09-已拒绝")
    public void testDecrease_HVPS_PR09() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO("dcep.181.001.01");

        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            Response<EnvelopeDTO<GwDTO>> response = fundingService.decrease(gwReqDTO);
            receiveHvpsNotify(response, "PR09", "HVPS0002", "HVPS报文检查失败");
        }
    }

    @Test
    @DisplayName("注资调减-PR18-已退回")
    public void testDecrease_HVPS_PR18() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO("dcep.181.001.01");

        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            Response<EnvelopeDTO<GwDTO>> response = fundingService.decrease(gwReqDTO);
            receiveHvpsNotify(response, "PR18", "HVPS0003", "HVPS日终退回");
        }
    }

    @Test
    @DisplayName("注资调减-PR04-已清算")
    public void testDecrease_HVPS_PR04() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO("dcep.181.001.01");
        log.info("asdfasdf");
        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            Response<EnvelopeDTO<GwDTO>> response = fundingService.decrease(gwReqDTO);
            receiveHvpsNotify(response, "PR04", null, null);
        }
    }

    /**
     * 预注资调减
     *   PR09-已拒绝
     *   PR18-已退回
     *   PR04-已清算
     */
    @Test
    @DisplayName("预注资调减-PR09-已拒绝")
    public void testPreDecrease_HVPS_PR09() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO("dcep.183.001.01");

        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            Response<EnvelopeDTO<GwDTO>> response = fundingService.decrease(gwReqDTO);
            receiveHvpsNotify(response, "PR09", "HVPS0002", "HVPS报文检查失败");
        }
    }

    @Test
    @DisplayName("预注资调减-PR18-已退回")
    public void testPreDecrease_HVPS_PR18() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO("dcep.183.001.01");

        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            Response<EnvelopeDTO<GwDTO>> response = fundingService.decrease(gwReqDTO);
            receiveHvpsNotify(response, "PR18", null, null);
        }
    }

    @Test
    @DisplayName("预注资调减-PR04-已清算")
    public void testPreDecrease_HVPS_PR04() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO("dcep.183.001.01");

        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            Response<EnvelopeDTO<GwDTO>> response = fundingService.decrease(gwReqDTO);
            receiveHvpsNotify(response, "PR04", null, null);
        }
    }

    @Test
    @DisplayName("预注资调减-PR18-已退回-PR04-已清算")
    public void testPreDecrease_HVPS_PR18_PR04() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO("dcep.183.001.01");

        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            Response<EnvelopeDTO<GwDTO>> response = fundingService.decrease(gwReqDTO);
            receiveHvpsNotify(response, "PR18", null, null);
            receiveHvpsNotify(response, "PR04", null, null);
        }
    }

    /**
     * 结算钱包清零
     *   PR12-清算排队
     *   PR09-已拒绝
     *   PR18-已退回
     *   PR04-已清算
     */
    @Test
    @DisplayName("结算钱包清零-PR12-清算排队")
    public void testZeroOut_HVPS_PR12() {
        String bizSts = "PR12";
        testZeroOut(bizSts);
    }

    @Test
    @DisplayName("结算钱包清零-PR09-已拒绝")
    public void testZeroOut_HVPS_PR09() {
        String bizSts = "PR09";
        testZeroOut(bizSts);
    }

    @Test
    @DisplayName("结算钱包清零-PR18-已退回")
    public void testZeroOut_HVPS_PR18() {
        String bizSts = "PR18";
        testZeroOut(bizSts);
    }

    @Test
    @DisplayName("结算钱包清零-PR04-已清算")
    public void testZeroOut_HVPS_PR04() {
        String bizSts = "PR04";
        testZeroOut(bizSts);
    }

    public void testZeroOut(String bizSts) {
        String actgDate = getActgDate();
        String transId = IdUtils.randomTransIdWithBizDt(actgDate);
        ZeroOutReqDTO zeroOutReqDTO = new ZeroOutReqDTO();
        zeroOutReqDTO.setTransId(transId);
        zeroOutReqDTO.setBizFlowNo("0000");
        zeroOutReqDTO.setMemberId(abcInstNo());
        zeroOutReqDTO.setCurrency("CNY");
        zeroOutReqDTO.setAmount(new BigDecimal("11.11"));
        zeroOutReqDTO.setAccountingDate(actgDate);

        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            try {
                Response<ZeroOutRespDTO> response = fundingService.zeroOutApply(zeroOutReqDTO);
                log.info("结算钱包清零申请，返回信息：{}", response);
            } catch (DcepException e) {
                log.error("结算钱包清零申请，抛出异常，{},{}", e.getCode(), e.getMessage());
            }

            log.info("=========================================查询大额原报文=========================================");
            HvpsTransDO hvpsTransDO = getHvpsTransDOByTransId(transId);

            log.info("=========================================模拟大额清算通知=========================================");
            ClearReportReqDTO clearReportReqDTO = new ClearReportReqDTO();
            clearReportReqDTO.setMsgId("2025101020000002");
            clearReportReqDTO.setMsgTp("saps.604.001.01");
            clearReportReqDTO.setOrgnlMsgId(hvpsTransDO.getHvpsMsgId());
            clearReportReqDTO.setOrgnlSendPty(hvpsTransDO.getHvpsSendPty());
            clearReportReqDTO.setOrgnlMsgTp(hvpsTransDO.getHvpsMsgTp());

            clearReportReqDTO.setPrcStatus(bizSts);
            clearReportReqDTO.setSettlementDate(DateUtils.convertToOutside(getHvpsSttlDate()));
            try {
                log.info("大额清算通知，返回信息：{}", clearReportReqDTO);
                Response<BizStatusDTO> notifyResponse = fundingService.hvpsReport(clearReportReqDTO);
                log.info("大额清算通知，返回信息：{}", notifyResponse);
            } catch (DcepException e) {
                log.error("大额清算通知，抛出异常，{},{}", e.getCode(), e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("结算钱包清零-PR04-已清算-只通知")
    public void testZeroOut_HVPS_PR04_OnlyNotify() {
        String transId = "202510300000000000958795598725000";

        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            log.info("=========================================查询大额原报文=========================================");
            HvpsTransDO hvpsTransDO = getHvpsTransDOByTransId(transId);

            log.info("=========================================模拟大额清算通知=========================================");
            ClearReportReqDTO clearReportReqDTO = new ClearReportReqDTO();
            clearReportReqDTO.setMsgId("2025101020000002");
            clearReportReqDTO.setMsgTp("saps.604.001.01");
            clearReportReqDTO.setOrgnlMsgId(hvpsTransDO.getHvpsMsgId());
            clearReportReqDTO.setOrgnlSendPty(hvpsTransDO.getHvpsSendPty());
            clearReportReqDTO.setOrgnlMsgTp(hvpsTransDO.getHvpsMsgTp());

            clearReportReqDTO.setPrcStatus("PR04");
            clearReportReqDTO.setSettlementDate(DateUtils.convertToOutside(getHvpsSttlDate()));
            try {
                log.info("大额清算通知，返回信息：{}", clearReportReqDTO);
                Response<BizStatusDTO> notifyResponse = fundingService.hvpsReport(clearReportReqDTO);
                log.info("大额清算通知，返回信息：{}", notifyResponse);
            } catch (DcepException e) {
                log.error("大额清算通知，抛出异常，{},{}", e.getCode(), e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("注资调增")
    public void testIncrease() {
        // 大额请求报文
        String hvpsSttlDate = getHvpsSttlDate();
        IncreaseReqDTO increaseReqDTO = new IncreaseReqDTO();
        increaseReqDTO.setMsgId(hvpsSttlDate + IdUtils.getRandomNum(8));
        increaseReqDTO.setMsgTp("hvps.112.001.01");
        increaseReqDTO.setClearingMemberId(abcHvpsClrBkNo());
        increaseReqDTO.setSendMemberId(abcHvpsClrBkNo());
        increaseReqDTO.setClearingSystemId("DCEP");
        increaseReqDTO.setReceiveMemberId(abcHvpsBkNo());
        increaseReqDTO.setCurrency("CNY");
        increaseReqDTO.setAmount(new BigDecimal("2.00"));
        increaseReqDTO.setAccountingDate(DateUtils.convertToOutside(hvpsSttlDate));

        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            try {
                Response<BizStatusDTO> response = fundingService.increase(increaseReqDTO);
                log.info("预注资调增，大额来账，返回信息：{}", response);
            } catch (DcepException e) {
                log.error("预注资调增，大额来账，抛出异常，{},{}", e.getCode(), e.getMessage());
            }
        }
    }

    /**
     * 预注资调增
     *   PR09-已拒绝，表示失败
     *   PR16-已冻结待清算，表示成功
     *   PR04-已清算，表示成功后，大额日间自动清算
     */
    @Test
    @DisplayName("预注资调增-PR09-已拒绝")
    public void testPreIncrease_HVPS_PR09() {
        List<BizStsDTO> bizStsList = new ArrayList<>();
        bizStsList.add(new BizStsDTO("PR09", "HVPS001", "HVPS处理失败，付款行头寸不足，冻结失败"));
        testPreIncrease(bizStsList);
    }

    @Test
    @DisplayName("预注资调增-PR16-已冻结待清算")
    public void testPreIncrease_HVPS_PR16() {
        List<BizStsDTO> bizStsList = new ArrayList<>();
        bizStsList.add(new BizStsDTO("PR16", "", ""));
        testPreIncrease(bizStsList);
    }

    @Test
    @DisplayName("预注资调增-PR04-已清算")
    public void testPreIncrease_HVPS_PR04() {
        List<BizStsDTO> bizStsList = new ArrayList<>();
        bizStsList.add(new BizStsDTO("PR04", "", ""));
        testPreIncrease(bizStsList);
    }

    @Test
    @DisplayName("预注资调增-PR16-已冻结待清算-PR04-已清算")
    public void testPreIncrease_HVPS_PR16_PR04() {
        List<BizStsDTO> bizStsList = new ArrayList<>();
        bizStsList.add(new BizStsDTO("PR16", "", ""));
        bizStsList.add(new BizStsDTO("PR04", "", ""));
        testPreIncrease(bizStsList);
    }

    public void testPreIncrease(List<BizStsDTO> bizDTOList) {
        // 大额请求报文
        String hvpsSttlDate = getHvpsSttlDate();
        IncreaseReqDTO increaseReqDTO = new IncreaseReqDTO();
        increaseReqDTO.setMsgId(hvpsSttlDate + IdUtils.getRandomNum(8));
        increaseReqDTO.setMsgTp("hvps.115.001.01");
        increaseReqDTO.setClearingMemberId(abcHvpsClrBkNo());
        increaseReqDTO.setSendMemberId(abcHvpsClrBkNo());
        increaseReqDTO.setClearingSystemId("DCEP");
        increaseReqDTO.setReceiveMemberId(abcHvpsBkNo());
        increaseReqDTO.setCurrency("CNY");
        increaseReqDTO.setAmount(new BigDecimal("11.11"));
        increaseReqDTO.setAccountingDate(DateUtils.convertToOutside(hvpsSttlDate));

        ClearReportReqDTO clearReportReqDTO = new ClearReportReqDTO();
        clearReportReqDTO.setMsgId(hvpsSttlDate + IdUtils.getRandomNum(8));
        clearReportReqDTO.setMsgTp("saps.604.001.01");
        clearReportReqDTO.setOrgnlMsgId(increaseReqDTO.getMsgId());
        clearReportReqDTO.setOrgnlMsgTp(increaseReqDTO.getMsgTp());
        clearReportReqDTO.setOrgnlSendPty(increaseReqDTO.getSendMemberId());
        clearReportReqDTO.setSettlementDate(DateUtils.convertToOutside(hvpsSttlDate));

        try (MockedStatic<InfoCacheUtil> mock = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockAllInfoCacheUtil(mock);

            try {
                Response<BizStatusDTO> response = fundingService.increase(increaseReqDTO);
                log.info("预注资调增，大额来账，返回信息：{}", response);
            } catch (DcepException e) {
                log.error("预注资调增，大额来账，抛出异常，{},{}", e.getCode(), e.getMessage());
            }

            for (int i = 0; i < bizDTOList.size(); i++) {
                BizStsDTO bizDTO = bizDTOList.get(i);

                clearReportReqDTO.setPrcStatus(bizDTO.getPrcStatus());
                clearReportReqDTO.setPrcCode(bizDTO.getPrcCode());
                clearReportReqDTO.setPrcInf(bizDTO.getPrcInf());
                try {
                    log.info("预注资调增，大额第{}次通知，通知请求：{}", i + 1, clearReportReqDTO);
                    Response<BizStatusDTO> response = fundingService.hvpsReport(clearReportReqDTO);
                    log.info("预注资调增，大额第{}次通知，通知应答：{}", i + 1, response);
                } catch (DcepException e) {
                    log.error("预注资调增，大额第{}次通知，抛出异常，{}，{}", i + 1, e.getCode(), e.getMessage());
                }
            }
        }
    }

    private HvpsTransDO getHvpsTransDOByTransId(String transId) {
        AccountingInstrDO accountingInstrDO = accountingInstrMapper.selectByPrimaryKey(new AccountingInstrDO(transId));
        if (null == accountingInstrDO) {
            log.error("查不到原请求数据:accountingInstrDO");
            throw new DcepException(ErrorEnum.NO_DATA_IN_DB_ERROR.getCode(), ErrorEnum.NO_DATA_IN_DB_ERROR.getDescription());
        }

        FundAdjustProdDO fundAdjustProdDO = fundAdjustProdMapper.selectByPrimaryKey(new FundAdjustProdDO(accountingInstrDO.getMsgId()));
        if (null == fundAdjustProdDO) {
            log.error("查不到原请求数据:fundAdjustProdDO");
            throw new DcepException(ErrorEnum.NO_DATA_IN_DB_ERROR.getCode(), ErrorEnum.NO_DATA_IN_DB_ERROR.getDescription());
        }

        HvpsTransDO hvpsTransDO = hvpsTransMapper.selectByMsgId(fundAdjustProdDO.getMsgId());
        if (null == hvpsTransDO) {
            log.error("查不到原请求数据:hvpsTransDO");
            throw new DcepException(ErrorEnum.NO_DATA_IN_DB_ERROR.getCode(), ErrorEnum.NO_DATA_IN_DB_ERROR.getDescription());
        }

        return hvpsTransDO;
    }

    private void mockAllInfoCacheUtil(MockedStatic<InfoCacheUtil> mock) {
        mock.when(() -> InfoCacheUtil.getHvpsClrBkNo(anyString())).thenReturn(abcHvpsClrBkNo());
        mock.when(() -> InfoCacheUtil.getHvpsBkNo(anyString())).thenReturn(abcHvpsBkNo());
        mock.when(() -> InfoCacheUtil.getInstNoForHvpsClrBkNo(anyString())).thenReturn(abcInstNo());
        mock.when(() -> InfoCacheUtil.getInstNoForHvpsBkNo(anyString())).thenReturn(abcInstNo());
        mock.when(InfoCacheUtil::getPbocHvpsClrBkNo).thenReturn(pbocHvpsClrBkNo());
        mock.when(InfoCacheUtil::getPbocDcepWlltId).thenReturn(pbocDecpWlltId());
        mock.when(() -> InfoCacheUtil.getCustodianInstNo(anyString())).thenReturn(custodianInstNo());
        mock.when(InfoCacheUtil::getPbocInf).thenReturn(pbocInstNo());
        mock.when(InfoCacheUtil::getPbocInnerCode).thenReturn("001");
        mock.when(() -> InfoCacheUtil.checkInstState(anyString())).thenReturn(true);
        mock.when(InfoCacheUtil::checkSysState).thenReturn(true);
        mock.when(InfoCacheUtil::checkSysState).thenReturn(true);

        OwnershipParam ownershipParam = new OwnershipParam();
        ownershipParam.setParmVal("001");
        ownershipParam.setParmDesc("测试摘要码");
        mock.when(() -> InfoCacheUtil.getPublicParam(anyString())).thenReturn(ownershipParam);
    }

    private void receiveHvpsNotify(Response<EnvelopeDTO<GwDTO>> response,
                                   String hvpsBizSts, String hvpsPrcCd, String hvpsPrcInf) {
        Dcep90000101DTO dcep900DTO = (Dcep90000101DTO) response.getResult().getSoapBody().getT();
        String orgnlMsgId = dcep900DTO.getOrgnlGrpHdr().getOrgnlMsgId();

        // 睡眠几秒，等待异步线程发送《准备金》成功
        TimeUtils.sleepSeconds(2);

        // 查询大额报文标识号
        HvpsTransDO hvpsTransDO = hvpsTransMapper.selectByMsgId(orgnlMsgId);
        if (null == hvpsTransDO) {
            log.error("等待准备金超时");
            throw new DcepException(ErrorEnum.MANAGER_RPC_TIMEOUT.getCode(), ErrorEnum.MANAGER_RPC_TIMEOUT.getDescription());
        }

        // 组装《准备金》应答报文
        ClearReportReqDTO clearReportReqDTO = new ClearReportReqDTO();
        clearReportReqDTO.setMsgId("2025101020000002");
        clearReportReqDTO.setMsgTp("saps.604.001.01");
        clearReportReqDTO.setOrgnlMsgId(hvpsTransDO.getHvpsMsgId());
        clearReportReqDTO.setOrgnlSendPty(hvpsTransDO.getHvpsSendPty());
        clearReportReqDTO.setOrgnlMsgTp(hvpsTransDO.getHvpsMsgTp());
        clearReportReqDTO.setPrcStatus(hvpsBizSts);
        clearReportReqDTO.setPrcCode(hvpsPrcCd);
        clearReportReqDTO.setPrcInf(hvpsPrcInf);
        clearReportReqDTO.setSettlementDate("2025-10-10");
        try {
            Response<BizStatusDTO> notifyResponse = fundingService.hvpsReport(clearReportReqDTO);
            log.info("大额清算通知处理，返回信息：{}", notifyResponse);
        } catch (DcepException e) {
            log.error("大额清算通知处理，抛出了异常，{},{}", e.getCode(), e.getMessage());
        }
    }

    public EnvelopeDTO<GwDTO> loadEnvelopeDTO(String msgTp) {
        String shortSender = "003"; // 农行
        String shortReceiver = "001"; // 运营中心

        String shortMsgTp = getShortMsgTp(msgTp);
        String xmlFile = String.format("funding/%s.xml", shortMsgTp);
        String msgId = TestUtils.getMsgId(shortSender, shortMsgTp);
        String creDtTm = TestUtils.getCurCreDtTm();

        // xml头
        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader soapHeader = TestUtils.createSoapHeader(msgTp, msgId, creDtTm, shortSender, shortReceiver);
        envelopeDTO.setSoapHeader(soapHeader);

        // xml体
        if ("181".equals(shortMsgTp)) {
            Dcep18100101DTO dcep181DTO = load181DTO(xmlFile, msgId, creDtTm, shortSender, Dcep18100101DTO.class);
            envelopeDTO.setSoapBody(new SoapBody<>(dcep181DTO));
            return envelopeDTO;
        }

        if ("183".equals(shortMsgTp)) {
            Dcep18300101DTO dcep183DTO = load183DTO(xmlFile, msgId, creDtTm, shortSender, Dcep18300101DTO.class);
            envelopeDTO.setSoapBody(new SoapBody<>(dcep183DTO));
            return envelopeDTO;
        }

        return null;
    }

    private Dcep18100101DTO load181DTO(String xmlFile, String msgId, String creDtTm, String shortSender, Class<Dcep18100101DTO> clazz) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{Sender}}", TestUtils.getFullOrgId(shortSender));

        return TestUtils.createDcepDTO(xmlFile, dataMap, clazz);
    }

    private Dcep18300101DTO load183DTO(String xmlFile, String msgId, String creDtTm, String shortSender, Class<Dcep18300101DTO> clazz) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{Sender}}", TestUtils.getFullOrgId(shortSender));

        return TestUtils.createDcepDTO(xmlFile, dataMap, clazz);
    }

    private String getShortMsgTp(String msgTp) {
        return msgTp.split("\\.")[1];
    }

    private String getHvpsSttlDate() {
        String curSysDt = systemStatusMapper.selectCurSysDt(Constant.SystemCode.WHOLESALE);
        return DateUtils.subtractDays(curSysDt, 1);
    }

    private String getActgDate() {
        String curSysDt = systemStatusMapper.selectCurSysDt(Constant.SystemCode.WHOLESALE);
        return DateUtils.subtractDays(curSysDt, -1);
    }

    // 农行在运营中心的托管机构号
    private String custodianInstNo() {
        return "C1010211000012";
    }

    // 农行在运营中心的机构号
    private String abcInstNo() {
        return "C1010311000014";
    }

    // 运营中心自己的机构号
    private String pbocInstNo() {
        return "G4001011000013";
    }

    // 农行在大额系统中直参行号（清算行号）
    private String abcHvpsClrBkNo() {
        return "103100000026";
    }

    // 运营中心在大额系统的直参行号（清算行号）
    private String pbocHvpsClrBkNo() {
        return "906100000076";
    }

    // 农行在大额系统中运营中心下的间参行号
    private String abcHvpsBkNo() {
        return "906100000092";
    }

    // 运营中心在《结算钱包》中的钱包ID
    private String pbocDecpWlltId() {
        return "101018696756";
    }

    @Data
    private static class BizStsDTO {
        private String prcStatus;
        private String prcCode;
        private String prcInf;
        public BizStsDTO(String prcStatus, String prcCode, String prcInf) {
            this.prcStatus = prcStatus;
            this.prcCode = prcCode;
            this.prcInf = prcInf;
        }
    }
}
