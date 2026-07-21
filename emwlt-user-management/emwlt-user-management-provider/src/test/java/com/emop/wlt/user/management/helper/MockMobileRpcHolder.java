package com.emop.wlt.user.management.helper;

import static org.mockito.Mockito.mockStatic;

import com.alipay.gateway.mobileservice.invoke.MobileRpcHolder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.mockito.MockedStatic;

public class MockMobileRpcHolder {

    private static MockedStatic<MobileRpcHolder> mobileRpcHolder;

    public static void buildMock() {
        mobileRpcHolder = mockStatic(MobileRpcHolder.class);
    }

    public static void mockMobileRpcHolder() {
        if (Objects.nonNull(mobileRpcHolder)) {
            Map<String, String> headers = new HashMap<>();
            headers.put("platform", "iOS");
            headers.put("did", "ZyiFRJpvH6gDAOI2Nh1Avg9d");
            headers.put("inner-version", "1.0.0");
            headers.put("environmentInformation", "{\"language\":\"zh-HK\"}");
            mobileRpcHolder.when(MobileRpcHolder::getHeaders).thenAnswer(invocation -> headers);
        }
    }

    public static void closeMobileRpcHolderMock() {
        if (Objects.nonNull(mobileRpcHolder)) {
            mobileRpcHolder.close();
        }
    }
}
