package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.ifaa.manager.IFAAServiceLdcManager;
import com.emop.wlt.ifaa.vo.CheckIfaaResultDTO;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.info.api.LoginPwdProvider;
import com.emop.wlt.user.management.exception.UserManageException;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.manager.UserManager;
import com.emop.wlt.user.management.model.request.Mapp03000101Req;
import com.emop.wlt.user.management.model.response.Mapp03000101Resp;
import com.emop.wlt.user.management.model.vo.BioAuthReqInfoVO;
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
public class LoginPwdVerifyProviderImplTest {

    @InjectMocks
    private LoginPwdVerifyProviderImpl loginPwdVerifyProvider;
    @Mock
    private UserManager userManager;
    @Mock
    private LoginPwdProvider loginPwdProvider;
    @Mock
    private IFAAServiceLdcManager IFAAServiceLdcManager;

    @BeforeClass
    public static void beforeClass() {
        MockFlowContext.buildFlowContextMock();
    }

    @Before
    public void before() {
        String configParam = ""
            + "{\n"
            + "    \"getCtxMobileNumber\": true,\n"
            + "    \"supportIfaa\": true\n"
            + "}";
        MockFlowContext.mockFlowContext(configParam);
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
    }

    @Test
    public void testLoginPasswordVerify1() {
        //正常-支持IFAA
        User user = new User();
        user.setUserId("123456");
        user.setPhone("+853-60001234");
        user.setPwd("password");
        user.setPwdSalt("salt");
        when(userManager.queryUserByPhone(anyString())).thenReturn(user);
        doNothing().when(loginPwdProvider).loginPwdVerify(any(),anyString());
        CheckIfaaResultDTO checkIfaaResultDTO = new CheckIfaaResultDTO();
        checkIfaaResultDTO.setIfaaResult("supportIFAA");
        when(IFAAServiceLdcManager.sendIFAADubboRequest(any(), anyString())).thenReturn(checkIfaaResultDTO);

        Mapp03000101Req mapp03000101Req = new Mapp03000101Req();
        mapp03000101Req.setPwdEnc("passwordEncrypted");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp03000101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03000101Req> request = RequestModelHelper.buildRequestModel(mapp03000101Req);
        ResponseModel<Mapp03000101Resp> result = loginPwdVerifyProvider.LoginPasswordVerify(request);
        Assert.assertEquals("supportIFAA", result.getMessageBody().getAuthRespMessage());
    }

    @Test
    public void testLoginPasswordVerify2() {
        //正常-不支持IFAA
        User user = new User();
        user.setUserId("123456");
        user.setPhone("+853-60001234");
        user.setPwd("password");
        user.setPwdSalt("salt");
        when(userManager.queryUserByPhone(anyString())).thenReturn(user);
        doNothing().when(loginPwdProvider).loginPwdVerify(any(),anyString());

        Mapp03000101Req mapp03000101Req = new Mapp03000101Req();
        mapp03000101Req.setPwdEnc("passwordEncrypted");
        RequestModel<Mapp03000101Req> request = RequestModelHelper.buildRequestModel(mapp03000101Req);
        ResponseModel<Mapp03000101Resp> result = loginPwdVerifyProvider.LoginPasswordVerify(request);
        Assert.assertNull(result.getMessageBody().getAuthRespMessage());
    }

    @Test
    public void testLoginPasswordVerify3() {
        //更新IFAA失败 authActionType与authMsgReq中的业务类型不一致
        User user = new User();
        user.setUserId("123456");
        user.setPhone("+853-60001234");
        user.setPwd("password");
        user.setPwdSalt("salt");
        when(userManager.queryUserByPhone(anyString())).thenReturn(user);
        doNothing().when(loginPwdProvider).loginPwdVerify(any(),anyString());

        Mapp03000101Req mapp03000101Req = new Mapp03000101Req();
        mapp03000101Req.setPwdEnc("passwordEncrypted");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/auth\"}");
        mapp03000101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03000101Req> request = RequestModelHelper.buildRequestModel(mapp03000101Req);
        ResponseModel<Mapp03000101Resp> result = loginPwdVerifyProvider.LoginPasswordVerify(request);
        Assert.assertNull(result.getMessageBody().getAuthRespMessage());
    }

    @Test
    public void testLoginPasswordVerify4() {
        //异常-账号未注册
        when(userManager.queryUserByPhone(anyString())).thenReturn(null);

        Mapp03000101Req mapp03000101Req = new Mapp03000101Req();
        mapp03000101Req.setPwdEnc("passwordEncrypted");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp03000101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03000101Req> request = RequestModelHelper.buildRequestModel(mapp03000101Req);
        Assert.assertThrows("账号未注册", UserManageException.class,
            ()->loginPwdVerifyProvider.LoginPasswordVerify(request));
    }

    @Test
    public void testLoginPasswordVerify5() {
        //异常-configParam为null
        MockFlowContext.mockFlowContext(null);

        User user = new User();
        user.setUserId("123456");
        user.setPhone("+853-60001234");
        user.setPwd("password");
        user.setPwdSalt("salt");
        when(userManager.queryUserByPhone(anyString())).thenReturn(user);
        doNothing().when(loginPwdProvider).loginPwdVerify(any(),anyString());

        Mapp03000101Req mapp03000101Req = new Mapp03000101Req();
        mapp03000101Req.setMobileNumber("+853-60001234");
        mapp03000101Req.setPwdEnc("passwordEncrypted");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp03000101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03000101Req> request = RequestModelHelper.buildRequestModel(mapp03000101Req);
        ResponseModel<Mapp03000101Resp> result = loginPwdVerifyProvider.LoginPasswordVerify(request);
        Assert.assertNull(result.getMessageBody().getAuthRespMessage());
    }

    @Test
    public void testLoginPasswordVerify6() {
        //异常-supportIfaa为false
        String configParam = ""
            + "{\n"
            + "    \"getCtxMobileNumber\": true,\n"
            + "    \"supportIfaa\": false\n"
            + "}";
        MockFlowContext.mockFlowContext(configParam);

        User user = new User();
        user.setUserId("123456");
        user.setPhone("+853-60001234");
        user.setPwd("password");
        user.setPwdSalt("salt");
        when(userManager.queryUserByPhone(anyString())).thenReturn(user);
        doNothing().when(loginPwdProvider).loginPwdVerify(any(),anyString());

        Mapp03000101Req mapp03000101Req = new Mapp03000101Req();
        mapp03000101Req.setPwdEnc("passwordEncrypted");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp03000101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03000101Req> request = RequestModelHelper.buildRequestModel(mapp03000101Req);
        ResponseModel<Mapp03000101Resp> result = loginPwdVerifyProvider.LoginPasswordVerify(request);
        Assert.assertNull(result.getMessageBody().getAuthRespMessage());
    }

    @Test
    public void testLoginPasswordVerify7() {
        //异常-authReqMessage为null
        User user = new User();
        user.setUserId("123456");
        user.setPhone("+853-60001234");
        user.setPwd("password");
        user.setPwdSalt("salt");
        when(userManager.queryUserByPhone(anyString())).thenReturn(user);
        doNothing().when(loginPwdProvider).loginPwdVerify(any(),anyString());

        Mapp03000101Req mapp03000101Req = new Mapp03000101Req();
        mapp03000101Req.setPwdEnc("passwordEncrypted");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage(null);
        mapp03000101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp03000101Req> request = RequestModelHelper.buildRequestModel(mapp03000101Req);
        ResponseModel<Mapp03000101Resp> result = loginPwdVerifyProvider.LoginPasswordVerify(request);
        Assert.assertNull(result.getMessageBody().getAuthRespMessage());
    }
}
