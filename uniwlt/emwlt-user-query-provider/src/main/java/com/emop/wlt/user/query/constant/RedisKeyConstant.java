package com.emop.wlt.user.query.constant;

/**
 * @author bobo
 * @Description:
 * @date 2021/7/15 下午3:39
 */
public class RedisKeyConstant {

    /**
     * 二维码解析redis缓存
     */
    public static final String QRPAY_DOMAIN_REDIS_KEY = "uniwlt:qrpay:domain";

    /**
     * 10分钟
     */
    public static final int QRPAY_DOMAIN_REDIS_KEY_EXPIRE = 10;

    /**
     * 限额redis key
     */
    public static final String LIMIT_AMOUNT_REDIS_KEY = "uniwlt:configuration:limit:amount";

    /**
     * 限额超时时间 24小时
     */
    public static final int LIMIT_AMOUNT_REDIS_KEY_EXPIRE = 3600;

    /**
     * 预付卡商户列表RedisKey uniwlt:prepaidCard:merchant:{isGray}:{cityCode}:{categoryId}
     */
    public static final String PREPAIDCARD_MERCHANT_LIST_REDIS_KEY = "uniwlt:prepaidCard:merchant:list:{}:{}:{}";

    /**
     * 预付卡商户列表缓存时间 30分钟
     */
    public static final int PREPAIDCARD_MERCHANT_LIST_REDIS_KEY_EXPIRE_MIN = 10;

    /**
     * 预付卡商户城市分类RedisKey uniwlt:prepaidCard:merchant:category:{isGray}:{cityCode}
     */
    public static final String PREPAIDCARD_MERCHANT_CATEGORY_REDIS_KEY = "uniwlt:prepaidCard:merchant:category:{}:{}:{}";

    /**
     * 预付卡商户城市分类缓存时间 10分钟
     */
    public static final int PREPAIDCARD_MERCHANT_CATEGORY_REDIS_KEY_EXPIRE_MIN = 10;

    /**
     * 预付卡商户排序缓存
     */
    public static final String PREPAIDCARD_MERCHANT_SORTING_REDIS_KEY = "uniwlt:prepaidCard:merchant:sorting";

    /**
     * 预付卡商户排序缓存时间 10分钟
     */
    public static final int PREPAIDCARD_MERCHANT_SORTING_REDIS_KEY_EXPIRE_MIN = 10;

    /**
     * 预付卡有效商户列表RedisKey uniwlt:normal:merchant:{isGray}
     */
    public static final String PREPAIDCARD_NORMAL_MERCHANT_LIST_REDIS_KEY = "uniwlt:normal:merchant:list:{}";

    /**
     * 预付卡有效商户列表缓存时间 10分钟
     */
    public static final int PREPAIDCARD_NORMAL_MERCHANT_LIST_REDIS_KEY_EXPIRE_MIN = 10;

    /**
     * 城市列表缓存key uniwlt:city:info:list:{cityInfoType}:{lang}:{grayFlag}
     */
    public static final String CITY_INFO_LIST_REDIS_KEY = "uniwlt:city:info:list:{}:{}:{}";

    /**
     * 城市列表缓存时间 10分钟
     */
    public static final int CITY_INFO_LIST_REDIS_KEY_MIN = 10;
}
