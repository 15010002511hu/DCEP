package com.dcep.supergw.common.constant;

public class Constant {

    /**
     * 签名验签分割符
     */
    public final static String CERT_SEPARATOR = "_";
    public final static String ENCRYPT_SUFFIX = "_e";
    public final static String SIGN_SUFFIX = "_s";
    public final static String PIPELINE_SEPARATOR = "|";

    public final static String ORG_UPDATE_FLAG = "00";

    public final static String CONST_ALGORITHM_TYPE = "02";

    /**
     * 编码格式
     */
    public final static String CHARTSET = "utf-8";

    public final static String HEADKEY = "Signature";

    public final static String RZONEGROUP = "default";

    /**
     * Dubbo超时
     */
    public final static int DUBBO_TIMEOUT = 3000;

    /**
     * CPU核数
     */
    public final static int CPU_NO = Runtime.getRuntime().availableProcessors();

    /**
     * 单CPU承载线程数
     */
    public final static int THREADS_PER_CPU = 10;

    public final static String CHANNEL_LOG_FLAG = "channel";

    public final static String SYSTEM = "hlht";

    /**
     * ChannelConext Attachment Key Constants
     */
    public final static String SIGNATURE = "signature";

    public final static String SERIALIZATION = "serialization";

    public final static String DESERIALIZATION = "deserialization";

    public final static String SOAP_HEADER = "soapheader";

    /**
     * 报文类型
     */
    public final static String MSGTP = "msgtp";

    public final static String OPEN_SYS = "opendcep";

    /**
     * url 地址
     */
    public final static String RECEIVER = "recevier";


    public static final String DCEP_302_001_01 = "dcep.302.001.01";
    public static final String DCEP_371_001_01 = "dcep.371.001.01";
    public static final String DCEP_711_001_01 = "dcep.711.001.01";


    /**
     * 策略
     */
    public static final String HARD_ENCRYPTION = "hardEncryption";
    public static final String SOFT_ENCRYPTION = "SOFTEncryption";
    public static final String HEADER_TRANSFER = "headerTransfer";
    public static final String BODY_TRANSFER = "bodyTransfer";

    public static final String GREY_TOTAL = "gray.scale.total";
    public static final String GREY_RATE = "gray.scale.grayRate";

    public static final String PRESS_FLAG = "perf_test_enabled";

    /**
     * HTTP头压测流量标识
     */
    public static final String LOAD_TRAFFIC_SWITCH_TRUE = "true";
    public static final String LOAD_TRAFFIC_SWITCH_FALSE = "false";

    /**
     * 环境位
     */
    public static final String ENV_FLAG = "action.config.envFlag";

    /**
     * 一体化测试标记位
     */
    public static final String HTTP_FLOW_GROUP = "DevGroup";
    public static final String HTTP_FLOW_ENV = "Environment";

    /**
     * 行业标识密钥索引
     */
    public static final String SECRET_INDUSTRY_KEY_LABEL = "secret.industry.key.label";

    /**
     * 是否交易参数配置临期的提示
     */
    public static final String TRADEPARAM_INVALID_WARNING_TYPE_LABEL = "tradeparam.invalid.warning.type";

    /**
     * 交易参数配置临期的提示提前天数
     */
    public static final String TRADEPARAM_INVALID_WARNING_DAYS_LABEL = "tradeparam.invalid.warning.days";
}
