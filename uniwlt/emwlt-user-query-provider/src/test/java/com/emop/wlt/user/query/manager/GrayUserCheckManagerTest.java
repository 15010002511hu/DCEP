package com.emop.wlt.user.query.manager;

import static org.mockito.ArgumentMatchers.anyString;

import com.emop.doms.cloudcontrol.query.api.UserInfoProvider;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class GrayUserCheckManagerTest {

    @InjectMocks
    private GrayUserCheckManager grayUserCheckManager;
    @Mock
    private UserInfoProvider userInfoProvider;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(grayUserCheckManager, "grayGroup", "appParamGray");
    }

    @Test
    public void isGray_1() {
        Assert.assertFalse(grayUserCheckManager.isGray(""));
    }

    @Test
    public void isGray_2() {
        List<String> userGroupCodes = new ArrayList<>();
        userGroupCodes.add("appParamGray");
        Mockito.when(userInfoProvider.getUserGroupCodes(anyString())).thenReturn(userGroupCodes);
        Assert.assertTrue(grayUserCheckManager.isGray("+853-60001234"));
    }

    @Test
    public void isGray_3() {
        List<String> userGroupCodes = new ArrayList<>();
        userGroupCodes.add("notGray");
        Mockito.when(userInfoProvider.getUserGroupCodes(anyString())).thenReturn(userGroupCodes);
        Assert.assertFalse(grayUserCheckManager.isGray("+853-60001234"));
    }

    @Test
    public void isGray_4() {
        Mockito.when(userInfoProvider.getUserGroupCodes(anyString())).thenThrow(new RuntimeException());
        Assert.assertFalse(grayUserCheckManager.isGray("+853-60001234"));
    }
}