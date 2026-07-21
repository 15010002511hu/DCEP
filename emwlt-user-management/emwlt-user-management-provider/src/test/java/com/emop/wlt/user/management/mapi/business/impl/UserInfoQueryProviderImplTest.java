package com.emop.wlt.user.management.mapi.business.impl;

import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp20800101Req;
import com.emop.wlt.user.management.model.response.Mapp20800101Resp;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.management.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserInfoQueryProviderImplTest {

    @InjectMocks
    private UserInfoQueryProviderImpl userInfoQueryProvider;
    @Mock
    private MappingIndexManagementService mappingIndexManagementService;
    @Mock
    private UserService userService;

    @Test
    public void testQueryUserInfo1() {
        //设置了登录密码
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        User user = new User();
        user.setPwd("password");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(user);

        Mapp20800101Req mapp20800101Req = new Mapp20800101Req();
        mapp20800101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp20800101Req> request = RequestModelHelper.buildRequestModel(mapp20800101Req);
        ResponseModel<Mapp20800101Resp> result = userInfoQueryProvider.queryUserInfo(request);
        Assert.assertTrue(result.getMessageBody().getSetPasswordFlag());
    }

    @Test
    public void testQueryUserInfo2() {
        //user为 null
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(null);

        Mapp20800101Req mapp20800101Req = new Mapp20800101Req();
        mapp20800101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp20800101Req> request = RequestModelHelper.buildRequestModel(mapp20800101Req);
        ResponseModel<Mapp20800101Resp> result = userInfoQueryProvider.queryUserInfo(request);
        Assert.assertFalse(result.getMessageBody().getSetPasswordFlag());
    }

    @Test
    public void testQueryUserInfo3() {
        //未设置登录密码
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        User user = new User();
        user.setPwd("");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(user);

        Mapp20800101Req mapp20800101Req = new Mapp20800101Req();
        mapp20800101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp20800101Req> request = RequestModelHelper.buildRequestModel(mapp20800101Req);
        ResponseModel<Mapp20800101Resp> result = userInfoQueryProvider.queryUserInfo(request);
        Assert.assertFalse(result.getMessageBody().getSetPasswordFlag());
    }

    @Test
    public void testQueryUserInfo4() {
        //账号未注册
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("");

        Mapp20800101Req mapp20800101Req = new Mapp20800101Req();
        mapp20800101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp20800101Req> request = RequestModelHelper.buildRequestModel(mapp20800101Req);
        ResponseModel<Mapp20800101Resp> result = userInfoQueryProvider.queryUserInfo(request);
        Assert.assertFalse(result.getMessageBody().getSetPasswordFlag());
    }
}
