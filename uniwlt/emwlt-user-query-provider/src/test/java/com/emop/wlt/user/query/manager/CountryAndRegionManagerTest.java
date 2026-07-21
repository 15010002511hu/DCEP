package com.emop.wlt.user.query.manager;

import static org.mockito.ArgumentMatchers.anyString;

import com.emop.infocache.ParamCache;
import com.emop.wlt.user.query.exception.UserQueryException;
import com.emop.wlt.user.query.helper.BusinessParamHelper;
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
public class CountryAndRegionManagerTest {

    @InjectMocks
    private CountryAndRegionManager countryAndRegionManager;
    @Mock
    private ParamCache paramCache;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(countryAndRegionManager, "top", "#");
        Mockito.when(paramCache.getComplexMultiList(anyString())).thenReturn(BusinessParamHelper.buildCountryAndAreaList());
    }

    @Test
    public void queryCountryAndRegionCode_BT00_ZHCN() {
        String expectedResp = "{\"#\":[{\"code\":\"+853\",\"countryCode\":\"MO\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"name\":\"中国澳门\"},{\"code\":\"+86\",\"countryCode\":\"CN\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"name\":\"中国内地\"},{\"code\":\"+852\",\"countryCode\":\"HK\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"name\":\"中国香港\"}]}";
        String actualResp = countryAndRegionManager.queryCountryAndRegionCode("zh-CN", "BT00");
        Assert.assertEquals(expectedResp, actualResp);
    }

    @Test
    public void queryCountryAndRegionCode_BT00_ZHHK() {
        String expectedResp = "{\"#\":[{\"code\":\"+853\",\"countryCode\":\"MO\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"name\":\"中國澳門\"},{\"code\":\"+86\",\"countryCode\":\"CN\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"name\":\"中國內地\"},{\"code\":\"+852\",\"countryCode\":\"HK\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"name\":\"中國香港\"}]}";
        String actualResp = countryAndRegionManager.queryCountryAndRegionCode("zh-HK", "BT00");
        Assert.assertEquals(expectedResp, actualResp);
    }

    @Test
    public void queryCountryAndRegionCode_BT00_ENUS() {
        String expectedResp = "{\"#\":[{\"code\":\"+853\",\"countryCode\":\"MO\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"name\":\"Macao,China\"},{\"code\":\"+86\",\"countryCode\":\"CN\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"name\":\"Mainland,China\"},{\"code\":\"+852\",\"countryCode\":\"HK\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"name\":\"Hong Kong,China\"}]}";
        String actualResp = countryAndRegionManager.queryCountryAndRegionCode("en-US", "BT00");
        Assert.assertEquals(expectedResp, actualResp);
    }

    @Test
    public void queryCountryAndRegionCode_BT01_ZHCN() {
        String expectedResp = "{\"#\":[{\"code\":\"+853\",\"countryCode\":\"MO\",\"firstLetter\":\"#\",\"isAllowTransfer\":\"1\",\"name\":\"中国澳门\"},{\"code\":\"+86\",\"countryCode\":\"CN\",\"firstLetter\":\"#\",\"isAllowTransfer\":\"1\",\"name\":\"中国内地\"},{\"code\":\"+852\",\"countryCode\":\"HK\",\"firstLetter\":\"#\",\"isAllowTransfer\":\"1\",\"name\":\"中国香港\"}]}";
        String actualResp = countryAndRegionManager.queryCountryAndRegionCode("zh-CN", "BT01");
        Assert.assertEquals(expectedResp, actualResp);
    }

    @Test
    public void queryCountryAndRegionCode_BT01_ZHHK() {
        String expectedResp = "{\"#\":[{\"code\":\"+853\",\"countryCode\":\"MO\",\"firstLetter\":\"#\",\"isAllowTransfer\":\"1\",\"name\":\"中國澳門\"},{\"code\":\"+86\",\"countryCode\":\"CN\",\"firstLetter\":\"#\",\"isAllowTransfer\":\"1\",\"name\":\"中國內地\"},{\"code\":\"+852\",\"countryCode\":\"HK\",\"firstLetter\":\"#\",\"isAllowTransfer\":\"1\",\"name\":\"中國香港\"}]}";
        String actualResp = countryAndRegionManager.queryCountryAndRegionCode("zh-HK", "BT01");
        Assert.assertEquals(expectedResp, actualResp);
    }
    @Test
    public void queryCountryAndRegionCode_BT01_ENUS() {
        String expectedResp = "{\"#\":[{\"code\":\"+853\",\"countryCode\":\"MO\",\"firstLetter\":\"#\",\"isAllowTransfer\":\"1\",\"name\":\"Macao,China\"},{\"code\":\"+86\",\"countryCode\":\"CN\",\"firstLetter\":\"#\",\"isAllowTransfer\":\"1\",\"name\":\"Mainland,China\"},{\"code\":\"+852\",\"countryCode\":\"HK\",\"firstLetter\":\"#\",\"isAllowTransfer\":\"1\",\"name\":\"Hong Kong,China\"}]}";
        String actualResp = countryAndRegionManager.queryCountryAndRegionCode("en-US", "BT01");
        Assert.assertEquals(expectedResp, actualResp);
    }

    @Test
    public void queryCountryAndRegionCode_BTXX() {
        String actualResp = countryAndRegionManager.queryCountryAndRegionCode("en-US", "BTXX");
        Assert.assertNull(actualResp);
    }

    @Test
    public void queryCountryAndRegionCodeException() {
        Assert.assertThrows("请求参数非法", UserQueryException.class,
            ()->countryAndRegionManager.queryCountryAndRegionCode("", "BT01"));
    }

    @Test
    public void queryMobileNumberLengthRule() {
        String expectedResp = "{\"transferMobileNumberLength\":{\"+86\":\"11\",\"+852\":\"8\",\"+853\":\"8\"},\"registerMobileNumberLength\":{\"+86\":\"11\",\"+852\":\"8\",\"+853\":\"8\"}}";
        String actualResp = countryAndRegionManager.queryMobileNumberLengthRule();
        Assert.assertEquals(expectedResp, actualResp);
    }

}