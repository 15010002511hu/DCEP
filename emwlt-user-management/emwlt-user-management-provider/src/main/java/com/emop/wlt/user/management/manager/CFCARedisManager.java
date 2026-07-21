package com.emop.wlt.user.management.manager;

import com.emop.wlt.common.util.Base64Utils;
import com.emop.wlt.redis.service.RedisOperator;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author bobo
 * @Description: cfca Redis操作类
 * @date 2022/3/29
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CFCARedisManager {

    public static final String CFCA_RANDOM_REDIS_KEY = "managagement:cafa:random:";
    public static final long CFCA_KEY_EXPIRE = 60;
    private final RedisOperator redisOperator;

    /**
     * 存储serverRandom
     *
     * @param serverRandomBytes
     */
    public String saveServerRandom(byte[] serverRandomBytes) {
        String serverRandom = Base64Utils.encode(serverRandomBytes);
        redisOperator.set(getRedisKey(serverRandom), "", CFCA_KEY_EXPIRE);
        return serverRandom;
    }

    /**
     * 判断serverRandom是否存在
     *
     * @param serverRandom
     */
    public boolean isExists(String serverRandom) {
        RBucket<String> rBucket = redisOperator.getRBucket(getRedisKey(serverRandom));
        boolean exists = redisOperator.isExists(rBucket);
        // 获取之后就要删除此key
        redisOperator.delAsync(rBucket);
        return exists;
    }

    private String getRedisKey(String key) {
        return CFCA_RANDOM_REDIS_KEY + key;
    }

}
