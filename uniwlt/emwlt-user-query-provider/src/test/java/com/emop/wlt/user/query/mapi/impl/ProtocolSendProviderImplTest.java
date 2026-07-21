package com.emop.wlt.user.query.mapi.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.emop.infocache.WalletCache;
import com.emop.infocache.api.dto.ReleaseHistoryDTO;
import com.emop.wlt.captcha.provider.EmailOperator;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.query.helper.ProtocolHelper;
import com.emop.wlt.user.query.helper.RequestModelHelper;
import com.emop.wlt.user.query.manager.GrayUserCheckManager;
import com.emop.wlt.user.query.model.request.Mapp20700101Req;
import com.emop.wlt.user.query.model.response.Mapp20700101Resp;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProtocolSendProviderImplTest {

    @InjectMocks
    private ProtocolSendProviderImpl protocolSendProvider;
    @Mock
    private WalletCache walletCache;
    @Mock
    private GrayUserCheckManager grayUserCheckManager;
    @Mock
    private EmailOperator emailOperator;

    @Before
    public void setUp() {
        ReleaseHistoryDTO releaseHistoryDTO = ProtocolHelper.buildProtocolData();
        Mockito.when(walletCache.getRelease("200")).thenReturn(releaseHistoryDTO);
    }
    @Test
    public void sendProtocolByEmail1() {
        //正常
        Mockito.when(grayUserCheckManager.isGray(anyString())).thenReturn(false);
        Mockito.doNothing().when(emailOperator).sendEmailWithAttachment(any());

        Mapp20700101Req mapp20700101Req = new Mapp20700101Req();
        mapp20700101Req.setMobileNumber("");
        mapp20700101Req.setEmail("lalaxy87@hotmail.com");
        mapp20700101Req.setLanguage("zh-HK");
        mapp20700101Req.setProtocolNo("20241014150751383");
        RequestModel<Mapp20700101Req> request = RequestModelHelper.buildRequestModel(mapp20700101Req);
        ResponseModel<Mapp20700101Resp> actualResponseModel = protocolSendProvider.sendProtocolByEmail(request);
        Assert.assertNotNull(actualResponseModel);
        Mockito.verify(emailOperator, Mockito.times(1)).sendEmailWithAttachment(any());
    }

    @Test
    public void sendProtocolByEmail2() {
        //发邮件异常
        Mockito.when(grayUserCheckManager.isGray(anyString())).thenReturn(false);
        Mockito.doThrow(new RuntimeException()).when(emailOperator).sendEmailWithAttachment(any());

        Mapp20700101Req mapp20700101Req = new Mapp20700101Req();
        mapp20700101Req.setMobileNumber("");
        mapp20700101Req.setEmail("lalaxy87@hotmail.com");
        mapp20700101Req.setLanguage("zh-HK");
        mapp20700101Req.setProtocolNo("20241014150751383");
        RequestModel<Mapp20700101Req> request = RequestModelHelper.buildRequestModel(mapp20700101Req);
        ResponseModel<Mapp20700101Resp> actualResponseModel = protocolSendProvider.sendProtocolByEmail(request);
        Assert.assertNotNull(actualResponseModel);
        Mockito.verify(emailOperator, Mockito.times(1)).sendEmailWithAttachment(any());
    }

    @Test
    public void sendProtocolByEmail3() {
        //异常-未找到协议
        Mockito.when(grayUserCheckManager.isGray(anyString())).thenReturn(false);

        Mapp20700101Req mapp20700101Req = new Mapp20700101Req();
        mapp20700101Req.setMobileNumber("");
        mapp20700101Req.setEmail("lalaxy87@hotmail.com");
        mapp20700101Req.setLanguage("zh-HK");
        mapp20700101Req.setProtocolNo("0000000000000");
        RequestModel<Mapp20700101Req> request = RequestModelHelper.buildRequestModel(mapp20700101Req);
        ResponseModel<Mapp20700101Resp> actualResponseModel = protocolSendProvider.sendProtocolByEmail(request);
        Assert.assertNotNull(actualResponseModel);
        Mockito.verify(emailOperator, Mockito.times(0)).sendEmailWithAttachment(any());
    }

    @Test
    public void getProtocolPair1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //异常-未知语言
        Mockito.when(grayUserCheckManager.isGray(anyString())).thenReturn(true);

        Method method = ProtocolSendProviderImpl.class.getDeclaredMethod("getProtocolPair", String.class, String.class, String.class);
        method.setAccessible(true);
        Pair<String, String> actualResp = (Pair<String, String>) method.invoke(protocolSendProvider,
            "+853-60001234", "unknown", "20241014150751383");
        Assert.assertEquals(Pair.of(null, null), actualResp);
    }

    @Test
    public void getProtocolPair2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //异常-未找到协议
        Mockito.when(grayUserCheckManager.isGray(anyString())).thenReturn(true);

        Method method = ProtocolSendProviderImpl.class.getDeclaredMethod("getProtocolPair", String.class, String.class, String.class);
        method.setAccessible(true);
        Pair<String, String> actualResp = (Pair<String, String>) method.invoke(protocolSendProvider,
            "+853-60001234", "zh-HK", "00000000000000000");
        Assert.assertEquals(Pair.of(null, null), actualResp);
    }

    @Test
    public void getProtocolPair3() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //异常-协议内容为空
        Mockito.when(walletCache.getRelease("200")).thenReturn(new ReleaseHistoryDTO());
        Mockito.when(grayUserCheckManager.isGray(anyString())).thenReturn(true);


        Method method = ProtocolSendProviderImpl.class.getDeclaredMethod("getProtocolPair", String.class, String.class, String.class);
        method.setAccessible(true);
        Pair<String, String> actualResp = (Pair<String, String>) method.invoke(protocolSendProvider,
            "+853-60001234", "zh-HK", "20241014150751383");
        Assert.assertEquals(Pair.of(null, null), actualResp);
    }

    @Test
    public void getSubjectNameByLanguageZHCN()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ProtocolSendProviderImpl.class.getDeclaredMethod("getSubjectNameByLanguage", String.class);
        method.setAccessible(true);
        String actualResp = (String) method.invoke(protocolSendProvider, "zh-CN");
        Assert.assertEquals("数字澳门元APP", actualResp);
    }

    @Test
    public void getSubjectNameByLanguageENUS()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ProtocolSendProviderImpl.class.getDeclaredMethod("getSubjectNameByLanguage", String.class);
        method.setAccessible(true);
        String actualResp = (String) method.invoke(protocolSendProvider, "en-US");
        Assert.assertEquals("", actualResp);
    }

    @Test
    public void getSubjectNameByLanguagePTPT()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ProtocolSendProviderImpl.class.getDeclaredMethod("getSubjectNameByLanguage", String.class);
        method.setAccessible(true);
        String actualResp = (String) method.invoke(protocolSendProvider, "pt-PT");
        Assert.assertEquals("", actualResp);
    }
}