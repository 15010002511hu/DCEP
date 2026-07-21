package com.emop.wlt.user.management.helper;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.flow.context.util.RegisterContextUtils;
import java.util.Objects;
import org.mockito.MockedStatic;

public class MockFlowContext {

    private static MockedStatic<FlowContextUtils> flowContextUtils;
    private static MockedStatic<RegisterContextUtils> registerContextUtils;

    public static void buildFlowContextMock() {
        flowContextUtils = mockStatic(FlowContextUtils.class);
    }

    public static void buildRegisterContextMock() {
        registerContextUtils = mockStatic(RegisterContextUtils.class);
    }

    public static void mockFlowContext(String configParam) {
        if (Objects.nonNull(flowContextUtils)) {
            flowContextUtils.when(()->FlowContextUtils.saveUserId(anyString())).thenAnswer(invocation -> null);
            flowContextUtils.when(()->FlowContextUtils.saveWalletId(anyString())).thenAnswer(invocation -> null);
            flowContextUtils.when(()->FlowContextUtils.saveMobileNumber(anyString())).thenAnswer(invocation -> null);
            flowContextUtils.when(()->FlowContextUtils.saveInstContext(anyString())).thenAnswer(invocation -> null);

            flowContextUtils.when(FlowContextUtils::getUserId).thenAnswer(invocation -> "123456");
            flowContextUtils.when(FlowContextUtils::getWalletId).thenAnswer(invocation -> "1122334455667788");
            flowContextUtils.when(FlowContextUtils::getInstContext).thenAnswer(invocation -> "instContext");
            flowContextUtils.when(FlowContextUtils::getMobileNumber).thenAnswer(invocation -> "+853-60001234");

            flowContextUtils.when(FlowContextUtils::getConfigParam).thenAnswer(invocationOnMock -> configParam);
        }
    }

    public static void mockRegisterContext(String countryRegionCode) {
        if (Objects.nonNull(registerContextUtils)) {
            registerContextUtils.when(()->RegisterContextUtils.saveMobileNumber(anyString())).thenAnswer(invocation -> null);
            registerContextUtils.when(()->RegisterContextUtils.saveCountryRegionCode(anyString())).thenAnswer(invocation -> null);

            registerContextUtils.when(RegisterContextUtils::getMobileNumber).thenAnswer(invocation -> "+853-60001234");
            registerContextUtils.when(RegisterContextUtils::getCountryRegionCode).thenAnswer(invocation -> countryRegionCode);
        }
    }

    public static void closeFlowContextMock() {
        if (Objects.nonNull(flowContextUtils)) {
            flowContextUtils.close();
        }
    }

    public static void closeRegisterContextMock() {
        if (Objects.nonNull(registerContextUtils)) {
            registerContextUtils.close();
        }
    }

}
