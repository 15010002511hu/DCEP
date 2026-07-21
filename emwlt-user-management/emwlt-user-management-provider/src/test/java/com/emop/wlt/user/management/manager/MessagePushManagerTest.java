package com.emop.wlt.user.management.manager;

import com.emop.wlt.message.push.api.AppMessageProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessagePushManagerTest {
    @InjectMocks
    private MessagePushManager messagePushManager;
    @Mock
    private AppMessageProvider appMessageProvider;

    @Test
    public void testSendForceLoginOutMsg() {
        doNothing().when(appMessageProvider).sendAppMessage(any());
        messagePushManager.sendForceLoginOutMsg("userId", "phone", "token");
        verify(appMessageProvider, times(1)).sendAppMessage(any());
    }

    @Test
    public void testSendResetLoginPwdMessage1() {
        //正常
        doNothing().when(appMessageProvider).sendAppMessage(any());
        messagePushManager.sendResetLoginPwdMessage("msgId", "userId", "mobileNumber");
        verify(appMessageProvider, times(1)).sendAppMessage(any());
    }

    @Test
    public void testSendResetLoginPwdMessage2() {
        //异常
        doThrow(new RuntimeException()).when(appMessageProvider).sendAppMessage(any());
        messagePushManager.sendResetLoginPwdMessage("msgId", "userId", "mobileNumber");
        verify(appMessageProvider, times(1)).sendAppMessage(any());
    }

    @Test
    public void testSendResetLoginPwdLogoutMessage1() {
        //正常
        doNothing().when(appMessageProvider).sendAppMessage(any());
        messagePushManager.sendResetLoginPwdLogoutMessage("msgId", "userId", "token",
            "deviceName");
        verify(appMessageProvider, times(1)).sendAppMessage(any());
    }

    @Test
    public void testSendResetLoginPwdLogoutMessage2() {
        //异常
        doThrow(new RuntimeException()).when(appMessageProvider).sendAppMessage(any());
        messagePushManager.sendResetLoginPwdLogoutMessage("msgId", "userId", "token",
            "deviceName");
        verify(appMessageProvider, times(1)).sendAppMessage(any());
    }

    @Test
    public void testSendResetLoginPwdLogoutMessage3() {
        //设备名称为空
        doNothing().when(appMessageProvider).sendAppMessage(any());
        messagePushManager.sendResetLoginPwdLogoutMessage("msgId", "userId", "token",
            "");
        verify(appMessageProvider, times(1)).sendAppMessage(any());
    }
}
