package com.emop.wlt.user.management.provider;

import com.emop.wlt.common.util.Base64Utils;
import com.emop.wlt.encrypt.operator.SecurityOperator;
import com.emop.wlt.user.management.config.CFCAConfig;
import com.emop.wlt.user.management.exception.UserManageBuzException;
import com.emop.wlt.user.management.manager.CFCARedisManager;
import java.nio.charset.StandardCharsets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CFCAManagerProviderImplTest {

    @InjectMocks
    private CFCAManagerProviderImpl cfcaManagerProvider;
    @Mock
    private SecurityOperator securityOperator;
    @Mock
    private CFCARedisManager cfcaRedisManager;
    @Mock
    private CFCAConfig cfcaConfig;

    @Before
    public void setUp() {
        String base64String = Base64Utils.encode("123".getBytes(StandardCharsets.UTF_8));
        when(securityOperator.decodeCFCAPassword(anyString(), any(), any(), any())).thenReturn(base64String);
    }

    @Test
    public void decryptPassWordTest() {
        when(cfcaRedisManager.isExists(anyString())).thenReturn(true);
        when(cfcaConfig.getPubKeyCertId(anyString())).thenReturn("123");
        String password = "MIIBCQICdTEEIMI5NbxXInHK0t+oZV0vyP7JSFPqWCg+W/mucVUROehBBBCXb65TwyiUtCIKMXWgfDDwBHsweQIgBOEMjmrgR2baykxJUabzWpSkscdu17YX+ZkOxXUioJMCIQDxEy2kIXvY0BUZyNCA110CQNThfXL9SoOEOUzHqLW1VwQgTeL4g0cfdfdDxc6tqbDWBcFdQZW/vmBGsVXzeMSBHgsEENSvqgUU1hWh9vpZJRF4064EMP8sj7wqYXtdjlzH0be9gjponjdK3a9yIqg+DUiYs9neXOCzV2Mzf+8wwKWy+ifwEgwgaU9TLXY3LjIuOS4xLWlQaG9uZTEzLDJAMC0xNC41LjE=";
        String passWord = cfcaManagerProvider.decryptPassWord(password);
        assertEquals("MzEzMjMz", passWord);
    }

    @Test
    public void decryptPassWordServerRandomExpireTest() {
        when(cfcaRedisManager.isExists(anyString())).thenReturn(false);
        String password = "MIIBCQICdTEEIMI5NbxXInHK0t+oZV0vyP7JSFPqWCg+W/mucVUROehBBBCXb65TwyiUtCIKMXWgfDDwBHsweQIgBOEMjmrgR2baykxJUabzWpSkscdu17YX+ZkOxXUioJMCIQDxEy2kIXvY0BUZyNCA110CQNThfXL9SoOEOUzHqLW1VwQgTeL4g0cfdfdDxc6tqbDWBcFdQZW/vmBGsVXzeMSBHgsEENSvqgUU1hWh9vpZJRF4064EMP8sj7wqYXtdjlzH0be9gjponjdK3a9yIqg+DUiYs9neXOCzV2Mzf+8wwKWy+ifwEgwgaU9TLXY3LjIuOS4xLWlQaG9uZTEzLDJAMC0xNC41LjE=";
        Assert.assertThrows("安全键盘秘钥过期", UserManageBuzException.class,
            ()-> cfcaManagerProvider.decryptPassWord(password));
    }

    @Test
    public void decryptPassWordNoCertIdTest() {
        when(cfcaRedisManager.isExists(anyString())).thenReturn(true);
        when(cfcaConfig.getPubKeyCertId(anyString())).thenReturn("");
        String password = "MIIBCQICdTEEIMI5NbxXInHK0t+oZV0vyP7JSFPqWCg+W/mucVUROehBBBCXb65TwyiUtCIKMXWgfDDwBHsweQIgBOEMjmrgR2baykxJUabzWpSkscdu17YX+ZkOxXUioJMCIQDxEy2kIXvY0BUZyNCA110CQNThfXL9SoOEOUzHqLW1VwQgTeL4g0cfdfdDxc6tqbDWBcFdQZW/vmBGsVXzeMSBHgsEENSvqgUU1hWh9vpZJRF4064EMP8sj7wqYXtdjlzH0be9gjponjdK3a9yIqg+DUiYs9neXOCzV2Mzf+8wwKWy+ifwEgwgaU9TLXY3LjIuOS4xLWlQaG9uZTEzLDJAMC0xNC41LjE=";
        Assert.assertThrows("证书不存在", UserManageBuzException.class,
            ()-> cfcaManagerProvider.decryptPassWord(password));
    }

}
