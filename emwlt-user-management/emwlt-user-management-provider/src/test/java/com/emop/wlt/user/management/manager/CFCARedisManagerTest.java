package com.emop.wlt.user.management.manager;

import static org.mockito.ArgumentMatchers.anyString;

import com.emop.wlt.common.util.Base64Utils;
import com.emop.wlt.common.util.RandomUtils;
import com.emop.wlt.redis.service.RedisOperator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CFCARedisManagerTest {

    @InjectMocks
    private CFCARedisManager cfcaRedisManager;
    @Mock
    private RedisOperator redisOperator;

    @Test
    public void saveServerRandomTest(){
        byte[] serverRandom = RandomUtils.randomBytes(16);
        String saveServerRandomBase64 = cfcaRedisManager.saveServerRandom(serverRandom);
        Assert.assertEquals(saveServerRandomBase64, Base64Utils.encode(serverRandom));
    }

    @Test
    public void isExistsTest(){
        Mockito.when(redisOperator.getRBucket(anyString())).thenReturn(null);
        boolean exists = cfcaRedisManager.isExists("");
        Assert.assertFalse(exists);
    }
}