package com.emop.wlt.user.management.mapi.business.impl;

import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.id.db.service.EmwltDBIdGenerator;
import com.emop.wlt.user.info.dto.TokenInfoDTO;
import com.emop.wlt.user.management.exception.UserManageException;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.MockMobileRpcHolder;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.manager.RegisterManager;
import com.emop.wlt.user.management.model.request.Mapp10000101Req;
import com.emop.wlt.user.management.model.request.Mapp10100101Req;
import com.emop.wlt.user.management.model.response.Mapp10000101Resp;
import com.emop.wlt.user.management.model.response.Mapp10100101Resp;
import com.emop.wlt.user.management.model.vo.Geolocation;
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
public class RegisterProviderImplTest {

    @InjectMocks
    private RegisterProviderImpl registerProviderImpl;
    @Mock
    private RegisterManager registerManager;
    @Mock
    private EmwltDBIdGenerator dbIdGenerator;

    @BeforeClass
    public static void beforeClass() {
        MockFlowContext.buildFlowContextMock();
        MockFlowContext.buildRegisterContextMock();
        MockMobileRpcHolder.buildMock();
    }

    @Before
    public void before() {
        MockFlowContext.mockFlowContext(null);
        MockFlowContext.mockRegisterContext("MO");
        MockMobileRpcHolder.mockMobileRpcHolder();
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
        MockFlowContext.closeRegisterContextMock();
        MockMobileRpcHolder.closeMobileRpcHolderMock();
    }

    @Test
    public void testRegisterValidation1() {
        //正常
        doNothing().when(registerManager).registerPreCheck(anyString(), any());

        Mapp10000101Req mapp10000101Req = new Mapp10000101Req();
        mapp10000101Req.setMobileNumber("+853-60001234");
        mapp10000101Req.setGeolocation(new Geolocation());
        mapp10000101Req.setCountryAndRegionCode("MO");
        RequestModel<Mapp10000101Req> request = RequestModelHelper.buildRequestModel(mapp10000101Req);
        ResponseModel<Mapp10000101Resp> result = registerProviderImpl.registerValidation(request);
        Assert.assertNotNull(result);
    }

    @Test
    public void testRegisterValidation2() {
        //异常 - countryAndRegionCodeEnum null
        doNothing().when(registerManager).registerPreCheck(anyString(), any());

        Mapp10000101Req mapp10000101Req = new Mapp10000101Req();
        mapp10000101Req.setMobileNumber("+853-60001234");
        mapp10000101Req.setGeolocation(new Geolocation());
        mapp10000101Req.setCountryAndRegionCode("ABC");
        RequestModel<Mapp10000101Req> request = RequestModelHelper.buildRequestModel(mapp10000101Req);
        Assert.assertThrows("请求参数非法", UserManageException.class,
            ()->registerProviderImpl.registerValidation(request));
    }

    @Test
    public void testRegisterValidation3() {
        //异常 - 手机号码与所属国别代码不匹配
        doNothing().when(registerManager).registerPreCheck(anyString(), any());

        Mapp10000101Req mapp10000101Req = new Mapp10000101Req();
        mapp10000101Req.setMobileNumber("+852-60001234");
        mapp10000101Req.setGeolocation(new Geolocation());
        mapp10000101Req.setCountryAndRegionCode("MO");
        RequestModel<Mapp10000101Req> request = RequestModelHelper.buildRequestModel(mapp10000101Req);
        Assert.assertThrows("手机号码与所属国别代码不匹配", UserManageException.class,
            ()->registerProviderImpl.registerValidation(request));
    }

    @Test
    public void testRegisterAndLogin1() {
        //正常
        TokenInfoDTO tokenInfoDTO = new TokenInfoDTO();
        tokenInfoDTO.setToken("token");
        tokenInfoDTO.setExpireSeconds(1800L);
        tokenInfoDTO.setRefreshIntervalSeconds(1800L);
        when(registerManager.register(any())).thenReturn(tokenInfoDTO);
        when(dbIdGenerator.getUserId(any(), any())).thenReturn("123456");

        RequestModel<Mapp10100101Req> request = RequestModelHelper.buildRequestModel(new Mapp10100101Req());
        ResponseModel<Mapp10100101Resp> result = registerProviderImpl.registerAndLogin(request);
        Assert.assertEquals("token", result.getMessageBody().getToken());
        Assert.assertEquals("1800", result.getMessageBody().getExpireSeconds());
        Assert.assertEquals("1800", result.getMessageBody().getRefreshIntervalSeconds());
        Assert.assertEquals("123456", result.getMessageBody().getUserId());
        Assert.assertEquals("+853-60001234", result.getMessageBody().getMobileNumber());
    }

    @Test
    public void testRegisterAndLogin2() {
        //countryAndRegionCodeEnum 为 null
        MockFlowContext.mockRegisterContext("ABC");

        TokenInfoDTO tokenInfoDTO = new TokenInfoDTO();
        tokenInfoDTO.setToken("token");
        tokenInfoDTO.setExpireSeconds(1800L);
        tokenInfoDTO.setRefreshIntervalSeconds(1800L);
        when(registerManager.register(any())).thenReturn(tokenInfoDTO);
        when(dbIdGenerator.getUserId(any(), any())).thenReturn("123456");

        RequestModel<Mapp10100101Req> request = RequestModelHelper.buildRequestModel(new Mapp10100101Req());
        ResponseModel<Mapp10100101Resp> result = registerProviderImpl.registerAndLogin(request);
        Assert.assertEquals("token", result.getMessageBody().getToken());
        Assert.assertEquals("1800", result.getMessageBody().getExpireSeconds());
        Assert.assertEquals("1800", result.getMessageBody().getRefreshIntervalSeconds());
        Assert.assertEquals("123456", result.getMessageBody().getUserId());
        Assert.assertEquals("+853-60001234", result.getMessageBody().getMobileNumber());
    }

    @Test
    public void testRegisterAndLogin3() {
        //异常 - 生成token失败
        when(registerManager.register(any())).thenThrow(new RuntimeException());
        when(dbIdGenerator.getUserId(any(), any())).thenReturn("123456");

        RequestModel<Mapp10100101Req> request = RequestModelHelper.buildRequestModel(new Mapp10100101Req());
        Assert.assertThrows("生成token失败", UserManageException.class,
            ()->registerProviderImpl.registerAndLogin(request));
    }
}
