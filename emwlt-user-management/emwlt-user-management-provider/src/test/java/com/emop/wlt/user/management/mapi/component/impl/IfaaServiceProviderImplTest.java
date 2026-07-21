package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.messageplatform.service.MessageSender;
import com.emop.wlt.captcha.service.SMSService;
import com.emop.wlt.captcha.util.SMSUtils;
import com.emop.wlt.captcha.util.SMSUtils.SvcType;
import com.emop.wlt.common.exception.EmwltBuzException;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.ifaa.manager.IFAAServiceLdcManager;
import com.emop.wlt.ifaa.model.BioAuthReqInfoDTO;
import com.emop.wlt.ifaa.vo.CheckIfaaResultDTO;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.MockMobileRpcHolder;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp03200101Req;
import com.emop.wlt.user.management.model.request.Mapp03400101Req;
import com.emop.wlt.user.management.model.response.Mapp03200101Resp;
import com.emop.wlt.user.management.model.response.Mapp03400101Resp;
import com.emop.wlt.user.management.model.vo.BioAuthReqInfoVO;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IfaaServiceProviderImplTest {

    @InjectMocks
    private IfaaServiceProviderImpl ifaaServiceProvider;
    @Mock
    private IFAAServiceLdcManager IFAAServiceLdcManager;
    @Mock
    private MappingIndexManagementService mappingIndexManagementService;
    @Mock
    private SMSService smsService;
    @Mock
    private MessageSender messageSender;

    @BeforeClass
    public static void beforeClass() {
        MockFlowContext.buildFlowContextMock();
        MockMobileRpcHolder.buildMock();
        SMSUtils.SVCTP_MAP.put("ST08", new SvcType());
        SMSUtils.SVCTP_MAP.put("ST09", new SvcType());
    }

    @Before
    public void before() {
        String configParam = "{\"authActionType\":\"AU00\"}";
        MockFlowContext.mockFlowContext(configParam);
        MockMobileRpcHolder.mockMobileRpcHolder();
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
        MockMobileRpcHolder.closeMobileRpcHolderMock();
    }

    @Test
    public void testDoBioAuth1() {
        //正常-不发短信
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        CheckIfaaResultDTO checkIfaaResultDTO = new CheckIfaaResultDTO();
        checkIfaaResultDTO.setIfaaResult("supportIFAA");
        checkIfaaResultDTO.setIfaaRespCode("0");
        when(IFAAServiceLdcManager.sendIFAADubboRequest(any(), anyString())).thenReturn(checkIfaaResultDTO);

        Mapp03200101Req mapp03200101Req = new Mapp03200101Req();
        mapp03200101Req.setMobileNumber("+853-60001234");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp03200101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03200101Req> request = RequestModelHelper.buildRequestModel(mapp03200101Req);
        ResponseModel<Mapp03200101Resp> result = ifaaServiceProvider.doBioAuth(request);
        Assert.assertEquals("supportIFAA", result.getMessageBody().getAuthRespMessage());
    }

    @Test
    public void testDoBioAuth2() {
        //IFAA流程配置参数和请求参数不一致
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");

        Mapp03200101Req mapp03200101Req = new Mapp03200101Req();
        mapp03200101Req.setMobileNumber("+853-60001234");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU02");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/auth\"}");
        mapp03200101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03200101Req> request = RequestModelHelper.buildRequestModel(mapp03200101Req);
        Assert.assertThrows("IFAA流程配置参数和请求参数不一致", EmwltBuzException.class,
            ()-> ifaaServiceProvider.doBioAuth(request));
    }

    @Test
    public void testDoBioAuth3() {
        //authActionType与authMsgReq中的业务类型不一致
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");

        Mapp03200101Req mapp03200101Req = new Mapp03200101Req();
        mapp03200101Req.setMobileNumber("+853-60001234");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/auth\"}");
        mapp03200101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03200101Req> request = RequestModelHelper.buildRequestModel(mapp03200101Req);
        Assert.assertThrows("authActionType与authMsgReq中的业务类型不一致", EmwltBuzException.class,
            ()-> ifaaServiceProvider.doBioAuth(request));
    }

    @Test
    public void testDoBioAuth4() {
        //IFAA流程配置参数有问题
        MockFlowContext.mockFlowContext(null);

        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");

        Mapp03200101Req mapp03200101Req = new Mapp03200101Req();
        mapp03200101Req.setMobileNumber("+853-60001234");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp03200101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03200101Req> request = RequestModelHelper.buildRequestModel(mapp03200101Req);
        Assert.assertThrows("IFAA流程配置参数有问题", EmwltBuzException.class,
            ()->ifaaServiceProvider.doBioAuth(request));
    }

    @Test
    public void testDoBioAuth5() {
        //异常-请求中AuthReqMessage为null
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");

        Mapp03200101Req mapp03200101Req = new Mapp03200101Req();
        mapp03200101Req.setMobileNumber("+853-60001234");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage(null);
        mapp03200101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03200101Req> request = RequestModelHelper.buildRequestModel(mapp03200101Req);
        Assert.assertThrows("请求参数非法", EmwltBuzException.class,
            ()->ifaaServiceProvider.doBioAuth(request));
    }

    @Test
    public void testDoBioQuery1() {
        //正常
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        CheckIfaaResultDTO checkIfaaResultDTO = new CheckIfaaResultDTO();
        checkIfaaResultDTO.setIfaaResult("supportIFAA");
        checkIfaaResultDTO.setIfaaRespCode("0");
        when(IFAAServiceLdcManager.sendIFAADubboRequest(any(), anyString())).thenReturn(checkIfaaResultDTO);

        Mapp03400101Req mapp03400101Req = new Mapp03400101Req();
        mapp03400101Req.setMobileNumber("+853-60001234");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU05");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/cap\"}");
        mapp03400101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03400101Req> request = RequestModelHelper.buildRequestModel(mapp03400101Req);
        ResponseModel<Mapp03400101Resp> result = ifaaServiceProvider.doBioQuery(request);
        Assert.assertEquals("supportIFAA", result.getMessageBody().getAuthRespMessage());
    }

    @Test
    public void testDoBioQuery2() {
        //不是查询请求AU05
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");

        Mapp03400101Req mapp03400101Req = new Mapp03400101Req();
        mapp03400101Req.setMobileNumber("+853-60001234");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp03400101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03400101Req> request = RequestModelHelper.buildRequestModel(mapp03400101Req);
        Assert.assertThrows("不是查询请求AU05", EmwltBuzException.class,
            ()-> ifaaServiceProvider.doBioQuery(request));
    }

    @Test
    public void testDoBioQuery3() {
        //authActionType与authMsgReq中的业务类型不一致
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");

        Mapp03400101Req mapp03400101Req = new Mapp03400101Req();
        mapp03400101Req.setMobileNumber("+853-60001234");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU05");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp03400101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03400101Req> request = RequestModelHelper.buildRequestModel(mapp03400101Req);
        Assert.assertThrows("authActionType与authMsgReq中的业务类型不一致", EmwltBuzException.class,
            ()-> ifaaServiceProvider.doBioQuery(request));
    }

    @Test
    public void testDoBioQuery4() {
        //异常-请求中AuthReqMessage为null
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");

        Mapp03400101Req mapp03400101Req = new Mapp03400101Req();
        mapp03400101Req.setMobileNumber("+853-60001234");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU05");
        bioAuthReqInfoVO.setAuthReqMessage(null);
        mapp03400101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03400101Req> request = RequestModelHelper.buildRequestModel(mapp03400101Req);
        Assert.assertThrows("请求参数非法", EmwltBuzException.class,
            ()->ifaaServiceProvider.doBioQuery(request));
    }

    @Test
    public void testSendSmsWithIfaaResult1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //正常 - 开通生物识别AU01
        BioAuthReqInfoDTO bioAuthReqInfoDTO = new BioAuthReqInfoDTO();
        bioAuthReqInfoDTO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoDTO.setAuthActionType("AU01");
        bioAuthReqInfoDTO.setAuthReqMessage("{\"action\":\"request/register\"}");
        CheckIfaaResultDTO checkIfaaResultDTO = new CheckIfaaResultDTO();
        checkIfaaResultDTO.setIfaaResult("ifaaResult");
        checkIfaaResultDTO.setIfaaRespCode("0");

        Method method = IfaaServiceProviderImpl.class.getDeclaredMethod("sendSmsWithIfaaResult",
            BioAuthReqInfoDTO.class, String.class, CheckIfaaResultDTO.class);
        method.setAccessible(true);
        method.invoke(ifaaServiceProvider, bioAuthReqInfoDTO, "+853-60001234", checkIfaaResultDTO);

        verify(smsService, times(1)).sendSMS(any());
    }

    @Test
    public void testSendSmsWithIfaaResult2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //正常 - 关闭生物识别AU04
        BioAuthReqInfoDTO bioAuthReqInfoDTO = new BioAuthReqInfoDTO();
        bioAuthReqInfoDTO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoDTO.setAuthActionType("AU04");
        bioAuthReqInfoDTO.setAuthReqMessage("{\"action\":\"request/register\"}");
        CheckIfaaResultDTO checkIfaaResultDTO = new CheckIfaaResultDTO();
        checkIfaaResultDTO.setIfaaResult("ifaaResult");
        checkIfaaResultDTO.setIfaaRespCode("0");

        Method method = IfaaServiceProviderImpl.class.getDeclaredMethod("sendSmsWithIfaaResult",
            BioAuthReqInfoDTO.class, String.class, CheckIfaaResultDTO.class);
        method.setAccessible(true);
        method.invoke(ifaaServiceProvider, bioAuthReqInfoDTO, "+853-60001234", checkIfaaResultDTO);

        verify(smsService, times(1)).sendSMS(any());
    }

    @Test
    public void testSendSmsWithIfaaResult3() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //不是成功结果
        BioAuthReqInfoDTO bioAuthReqInfoDTO = new BioAuthReqInfoDTO();
        bioAuthReqInfoDTO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoDTO.setAuthActionType("AU01");
        bioAuthReqInfoDTO.setAuthReqMessage("{\"action\":\"request/register\"}");
        CheckIfaaResultDTO checkIfaaResultDTO = new CheckIfaaResultDTO();
        checkIfaaResultDTO.setIfaaResult("ifaaResult");
        checkIfaaResultDTO.setIfaaRespCode("1");

        Method method = IfaaServiceProviderImpl.class.getDeclaredMethod("sendSmsWithIfaaResult",
            BioAuthReqInfoDTO.class, String.class, CheckIfaaResultDTO.class);
        method.setAccessible(true);
        method.invoke(ifaaServiceProvider, bioAuthReqInfoDTO, "+853-60001234", checkIfaaResultDTO);

        verify(smsService, times(0)).sendSMS(any());
    }

    @Test
    public void testSendSmsWithIfaaResult4() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //不是成功结果
        BioAuthReqInfoDTO bioAuthReqInfoDTO = new BioAuthReqInfoDTO();
        bioAuthReqInfoDTO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoDTO.setAuthActionType("AU05");
        bioAuthReqInfoDTO.setAuthReqMessage("{\"action\":\"request/register\"}");
        CheckIfaaResultDTO checkIfaaResultDTO = new CheckIfaaResultDTO();
        checkIfaaResultDTO.setIfaaResult("ifaaResult");
        checkIfaaResultDTO.setIfaaRespCode("0");

        Method method = IfaaServiceProviderImpl.class.getDeclaredMethod("sendSmsWithIfaaResult",
            BioAuthReqInfoDTO.class, String.class, CheckIfaaResultDTO.class);
        method.setAccessible(true);
        method.invoke(ifaaServiceProvider, bioAuthReqInfoDTO, "+853-60001234", checkIfaaResultDTO);

        verify(smsService, times(0)).sendSMS(any());
    }

    @Test
    public void testSendSmsWithIfaaResult5() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //发短信异常-手机号格式异常
        BioAuthReqInfoDTO bioAuthReqInfoDTO = new BioAuthReqInfoDTO();
        bioAuthReqInfoDTO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoDTO.setAuthActionType("AU04");
        bioAuthReqInfoDTO.setAuthReqMessage("{\"action\":\"request/register\"}");
        CheckIfaaResultDTO checkIfaaResultDTO = new CheckIfaaResultDTO();
        checkIfaaResultDTO.setIfaaResult("ifaaResult");
        checkIfaaResultDTO.setIfaaRespCode("0");

        Method method = IfaaServiceProviderImpl.class.getDeclaredMethod("sendSmsWithIfaaResult",
            BioAuthReqInfoDTO.class, String.class, CheckIfaaResultDTO.class);
        method.setAccessible(true);
        method.invoke(ifaaServiceProvider, bioAuthReqInfoDTO, "+85360001234", checkIfaaResultDTO);

        verify(smsService, times(0)).sendSMS(any());
    }
}
