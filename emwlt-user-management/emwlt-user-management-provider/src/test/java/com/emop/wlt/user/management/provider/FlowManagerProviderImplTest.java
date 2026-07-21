package com.emop.wlt.user.management.provider;

import static org.mockito.ArgumentMatchers.anyString;

import com.emop.wlt.flow.entity.FlowContext;
import com.emop.wlt.flow.manager.FlowContextManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FlowManagerProviderImplTest {

    @InjectMocks
    private FlowManagerProviderImpl flowManagerProviderImpl;
    @Mock
    private FlowContextManager flowContextManager;

    @Test
    public void testSelectSuccessVerifySceneType1() {
        FlowContext flowContext = new FlowContext();
        flowContext.setVerifySceneType("VST00");
        when(flowContextManager.getForkCacheFlowContext(anyString())).thenReturn(flowContext);

        String result = flowManagerProviderImpl.selectSuccessVerifySceneType("contextNo");
        Assert.assertEquals("VST00", result);
    }

    @Test
    public void testSelectSuccessVerifySceneType2() {
       when(flowContextManager.getForkCacheFlowContext(anyString())).thenReturn(null);

        String result = flowManagerProviderImpl.selectSuccessVerifySceneType("contextNo");
        Assert.assertNull(result);
    }
}