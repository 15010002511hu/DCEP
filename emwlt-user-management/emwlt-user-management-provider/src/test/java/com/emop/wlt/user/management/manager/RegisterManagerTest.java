package com.emop.wlt.user.management.manager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.emop.infocache.ParamCache;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.info.api.UserInfoProvider;
import com.emop.wlt.user.info.dto.TokenInfoDTO;
import com.emop.wlt.user.management.exception.UserManageException;
import com.emop.wlt.user.management.helper.BusinessParamHelper;
import com.emop.wlt.user.management.model.RegisterDTO;
import com.emop.wlt.user.management.model.vo.Geolocation;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.management.service.UserService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
public class RegisterManagerTest {

    @InjectMocks
    private RegisterManager registerManager;
    @Mock
    private MappingIndexManagementService mappingIndexManagementService;
    @Mock
    private UserService userService;
    @Mock
    private UserInfoProvider userInfoProvider;
    @Mock
    private ParamCache paramCache;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(registerManager, "registerAreaList", "[\"澳门\"]");
        ReflectionTestUtils.setField(registerManager, "registerAreaVerify", true);
    }

    @Test
    public void registerPreCheckTest() {
        Geolocation geolocation = new Geolocation();
        geolocation.setCountry("中国");
        geolocation.setProvince("澳门特别行政区");
        geolocation.setCity("澳门");
        registerManager.registerPreCheck("+853-60001234", geolocation);
        Mockito.verify(mappingIndexManagementService, Mockito.times(1))
            .selectByPhone(anyString());
    }

    @Test
    public void registerTest() {
        RegisterDTO registerDTO = RegisterDTO.builder()
            .userId("123456")
            .mobileNumber("+86-13512345678")
            .deviceId("deviceId")
            .deviceType("deviceType")
            .countryAndRegionCode("CN")
            .innerVersion("inner-version").build();
        TokenInfoDTO excepted = new TokenInfoDTO();
        excepted.setToken("token");
        excepted.setExpireSeconds(60*60L);
        excepted.setRefreshIntervalSeconds(60*60L);
        Mockito.when(userInfoProvider.registerAndLoginV2(any())).thenReturn(excepted);
        TokenInfoDTO actual = registerManager.register(registerDTO);
        Assert.assertEquals(excepted, actual);
    }

    @Test
    public void getLoginTokenV2Test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TokenInfoDTO excepted = new TokenInfoDTO();
        excepted.setToken("token");
        excepted.setExpireSeconds(60*60L);
        excepted.setRefreshIntervalSeconds(60*60L);
        Mockito.when(userInfoProvider.registerAndLoginV2(any())).thenReturn(excepted);

        Method method = RegisterManager.class.getDeclaredMethod("getLoginTokenV2",
            String.class, String.class, String.class, String.class);
        method.setAccessible(true);
        TokenInfoDTO actual = (TokenInfoDTO) method.invoke(registerManager,
            "123456", "+86-13512345678", "deviceId", "deviceType");
        Assert.assertEquals(excepted, actual);
    }

    @Test
    public void checkRegistedTest_1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Mockito.when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        Method method = RegisterManager.class.getDeclaredMethod("checkRegistered", String.class);
        method.setAccessible(true);
        method.invoke(registerManager, "+86-13512345678");

        Mockito.verify(userService, Mockito.times(1))
            .selectByPrimaryKey(anyString());
    }

    @Test
    public void checkRegistedTest_2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Mockito.when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("");
        Method method = RegisterManager.class.getDeclaredMethod("checkRegistered", String.class);
        method.setAccessible(true);
        method.invoke(registerManager, "+86-13512345678");

        Mockito.verify(userService, Mockito.times(0))
            .selectByPrimaryKey(anyString());
    }

    @Test(expected = UserManageException.class)
    public void checkRegistedTest_3() throws Throwable {
        Mockito.when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        Mockito.when(userService.selectByPrimaryKey(anyString())).thenReturn(new User());
        Method method = RegisterManager.class.getDeclaredMethod("checkRegistered", String.class);
        method.setAccessible(true);
        try {
            method.invoke(registerManager, "+86-13512345678");
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    public void checkLocationInfoTest_1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Geolocation geolocation = new Geolocation();
        geolocation.setCountry("中国");
        geolocation.setProvince("澳门特别行政区");
        geolocation.setCity("澳门");
        Method method = RegisterManager.class.getDeclaredMethod("checkLocationInfo", Geolocation.class);
        method.setAccessible(true);
        method.invoke(registerManager, geolocation);
    }

    @Test(expected = UserManageException.class)
    public void checkLocationInfoTest_2() throws Throwable {
        Geolocation geolocation = new Geolocation();
        geolocation.setCountry("加拿大");
        geolocation.setProvince("安大略省");
        geolocation.setCity("多伦多");
        Method method = RegisterManager.class.getDeclaredMethod("checkLocationInfo", Geolocation.class);
        method.setAccessible(true);
        try {
            method.invoke(registerManager, geolocation);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    public void checkMobileNumberFormatTest_1()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Mockito.when(paramCache.getComplexMultiList(anyString())).thenReturn(BusinessParamHelper.buildCountryAndAreaList());

        Method method = RegisterManager.class.getDeclaredMethod("checkMobileNumberFormat", String.class);
        method.setAccessible(true);
        method.invoke(registerManager, "+853-68955050");
    }

    @Test(expected = UserManageException.class)
    public void checkMobileNumberFormatTest_2()
        throws Throwable {
        Mockito.when(paramCache.getComplexMultiList(anyString())).thenReturn(BusinessParamHelper.buildCountryAndAreaList());

        Method method = RegisterManager.class.getDeclaredMethod("checkMobileNumberFormat", String.class);
        method.setAccessible(true);
        try {
            method.invoke(registerManager, "+853-10001234");
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}