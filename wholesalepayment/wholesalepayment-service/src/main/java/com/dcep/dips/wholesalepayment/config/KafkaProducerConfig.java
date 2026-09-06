package com.dcep.dips.wholesalepayment.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.dcep.common.nacos.DcepListener;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * kafka生产者配置
 * @author: chenxighnfeng
 */
@Slf4j
//@Configuration
//@EnableKafka
public class KafkaProducerConfig {

    private static final String KAFKA_NACOS_DATAID = "com.dcep.wholesale.properties";

    private static final String KAFKA_NACOS_GROUP = "DEFAULT_GROUP";

    private static final String KAFKA_SERVERS_KEY = "kafka_producer_servers";

    private static final String KAFKA_TIMEOUT_KEY = "kafka_producer_timeout";

    private static final String KAFKA_USERNAME_KEY = "kafka_producer_username";

    private static final String KAFKA_PASS_KEY = "kafka_producer_pa" + "ssword";
    /**
     * kafka集群IP
     */
    private String servers;

    /**
     * producer超时时间，单位毫秒
     */
    private int timeOut;

    /**
     * kafka鉴权用户
     */
    private String kafkaUser;

    /**
     * kafka鉴权密码
     */
    private String kafkaPass;

    @Value("${nacos.config.server-addr}")
    private String serverAddr;

    @Value("${nacos.config.namespace}")
    private String namespace;

    @Value("${nacos.config.username}")
    private String username;

    @Value("${nacos.config.password}")
    private String password;

    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 1(默认)：这意味着producer在ISR中的leader已成功收到的数据并得到确认后发送下一条message。如果leader宕机了，则会丢失数据。
     * 0：这意味着producer无需等待来自broker的确认而继续发送下一批消息。这种情况下数据传输效率最高，但是数据可靠性确是最低的。
     * -1：producer需要等待ISR中的所有follower都确认接收到数据后才算一次发送完成，可靠性最高。
     */
    private final String acks = "-1";

    /**
     * producer发送失败后的重试次数，默认0
     */
    private final int retries = 0;

    /**
     * producer将发送给同一个partition的消息进行批处理，减少请求次数提高client与server之间性能，单位字节
     */
    private final int batchSize = 4096;

    /**
     * producer将在设定的延迟时间后进行发送，单位毫秒
     */
    private final int linger = 1;

    /**
     * producer可以用来缓存数据的内存大小。如果数据产生速度大于向broker发送的速度，producer会阻塞或者抛出异常。
     */
    private final int bufferMemory = 40960;

    /**
     * kafka中间件sasl鉴权配置，固定写死
     */
    private final String securityProtocol = "SASL_PLAINTEXT";
    /**
     * kafka中间件sasl鉴权配置，固定写死
     */
    private final String saslMechanism = "PLAIN";

    public Map<String, Object> producerConfigs() {
        // 拼装kafka中间件sasl鉴权用户密码配置信息
        String saslJaasConfig = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
            + kafkaUser + "\" pa" + "ssword=\"" + kafkaPass + "\";";

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, timeOut);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
        return props;
    }

    public DefaultKafkaProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        kafkaTemplate = new KafkaTemplate<String, String>(producerFactory());
        return kafkaTemplate;
    }

    @PostConstruct
    public void init() throws NacosException, IOException {
        Properties props = new Properties();
        props.put("serverAddr", serverAddr);
        props.put("namespace", namespace);
        props.put("username", username);
        props.put("password", password);
        ConfigService configService = NacosFactory.createConfigService(props);
        String context = configService.getConfig(KAFKA_NACOS_DATAID, KAFKA_NACOS_GROUP, 1000);
        if (context != null) {
            try {
                Properties clearprodProps = new Properties();
                clearprodProps.load(new StringReader(context));
                servers = (String) clearprodProps.get(KAFKA_SERVERS_KEY);
                timeOut = Integer.valueOf((String) clearprodProps.get(KAFKA_TIMEOUT_KEY));
                kafkaUser = (String) clearprodProps.get(KAFKA_USERNAME_KEY);
                kafkaPass = (String) clearprodProps.get(KAFKA_PASS_KEY);
                log.info("kafka集群IP:{}, producer超时ms:{}", servers, timeOut);
            } catch (Exception e) {
                log.error("fail to load clearprod.properties", e);
                throw e;
            }
        }

        configService.addListener(KAFKA_NACOS_DATAID, KAFKA_NACOS_GROUP, new DcepListener(10000) {
            @Override
            public void onReceived(String context) throws Exception {
                if (context != null) {
                    Properties clearprodProps = new Properties();
                    try {
                        clearprodProps.load(new StringReader(context));
                        servers = (String) clearprodProps.get(KAFKA_SERVERS_KEY);
                        timeOut = Integer.valueOf((String) clearprodProps.get(KAFKA_TIMEOUT_KEY));
                        kafkaUser = (String) clearprodProps.get(KAFKA_USERNAME_KEY);
                        kafkaPass = (String) clearprodProps.get(KAFKA_PASS_KEY);
                        log.info("开始将kafka集群IP更换为:{}, producer超时ms:{}", servers, timeOut);

                        // 停止DefaultKafkaProducerFactory
                        kafkaTemplate.flush();
                        DefaultKafkaProducerFactory<String, String> producerFactory = (DefaultKafkaProducerFactory<String, String>) kafkaTemplate
                            .getProducerFactory();
                        Field threadBoundProducersField = producerFactory.getClass()
                            .getDeclaredField("threadBoundProducers");
                        threadBoundProducersField.setAccessible(true);
                        Object threadBoundProducers = threadBoundProducersField.get(producerFactory);
                        if (threadBoundProducers != null) {
                            // 关闭线程独立producer
                            producerFactory.closeThreadBoundProducer();
                        }
                        producerFactory.reset();

                        // 更换DefaultKafkaProducerFactory
                        Field producerFactoryField = kafkaTemplate.getClass().getDeclaredField("producerFactory");
                        producerFactoryField.setAccessible(true);
                        DefaultKafkaProducerFactory<String, String> newProducerFactory = producerFactory();
                        producerFactoryField.set(kafkaTemplate, newProducerFactory);

                        log.info("kafka集群IP成功更新为:{}, producer超时ms:{}", servers, timeOut);

                    } catch (Exception e) {
                        log.error("更新kafka集群IP异常", e);
                        throw e;
                    }
                }
            }
        });
    }

}
