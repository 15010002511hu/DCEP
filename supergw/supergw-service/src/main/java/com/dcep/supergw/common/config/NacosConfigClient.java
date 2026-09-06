package com.dcep.supergw.common.config;

import com.dcep.supergw.common.constant.Constant;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author maxinyu
 * @date 2023/6/19 17:48
 */
public class NacosConfigClient {

    private static final Logger LOG = LoggerFactory.getLogger(NacosConfigClient.class);

    private Integer total;

    private Integer grayRate;

    private Boolean pressFlag;

    private String envFlag;

    private String industryKeyLabel;

    /**
     * 交易参数配置是否校验和告警
     */
    private Integer tradeparamInvalidWarningType;

    private Integer tradeparamInvalidWarningDays;

    private static volatile NacosConfigClient client = null;

    public static NacosConfigClient getInstance() {
        if (client == null) {
            synchronized (NacosConfigClient.class) {
                if (client == null) {
                    client = new NacosConfigClient();
                }
            }
        }
        return client;
    }

    public void refresh(String meta) {
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(meta.getBytes()));
            total = Integer.parseInt(properties.getProperty(Constant.GREY_TOTAL));
            grayRate = Integer.parseInt(properties.getProperty(Constant.GREY_RATE));
            envFlag = properties.getProperty(Constant.ENV_FLAG);
            industryKeyLabel = properties.getProperty(Constant.SECRET_INDUSTRY_KEY_LABEL);

            // 交易参数相关的初始化
            tradeparamInvalidWarningType = Integer.parseInt(properties.getProperty(Constant.TRADEPARAM_INVALID_WARNING_TYPE_LABEL));
            tradeparamInvalidWarningDays = Integer.parseInt(properties.getProperty(Constant.TRADEPARAM_INVALID_WARNING_DAYS_LABEL));

            LOG.info("nacos配置刷新成功：{}", this.toString());
        } catch (IOException e) {
            LOG.error("解析nacos配置异常:{}", meta);
        }
    }

    public void refreshPress(String meta) {
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(meta.getBytes()));
            pressFlag = Boolean.parseBoolean(properties.getProperty(Constant.PRESS_FLAG));
            LOG.info("压测标志刷新成功：{}", this.toString());
        } catch (IOException e) {
            LOG.error("压测标志解析异常:{}", meta);
        }
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getGrayRate() {
        return grayRate;
    }

    public void setGrayRate(Integer grayRate) {
        this.grayRate = grayRate;
    }

    public Boolean getPressFlag() {
        return pressFlag;
    }

    public void setPressFlag(Boolean pressFlag) {
        this.pressFlag = pressFlag;
    }

    public String getEnvFlag() {
        return envFlag;
    }

    public void setEnvFlag(String envFlag) {
        this.envFlag = envFlag;
    }

    public String getIndustryKeyLabel() {
        return industryKeyLabel;
    }

    public void setIndustryKeyLabel(String industryKeyLabel) {
        this.industryKeyLabel = industryKeyLabel;
    }

    public Integer getTradeparamInvalidWarningType() {
        return tradeparamInvalidWarningType;
    }

    public void setTradeparamInvalidWarningType(Integer tradeparamInvalidWarningType) {
        this.tradeparamInvalidWarningType = tradeparamInvalidWarningType;
    }

    public Integer getTradeparamInvalidWarningDays() {
        return tradeparamInvalidWarningDays;
    }

    public void setTradeparamInvalidWarningDays(Integer tradeparamInvalidWarningDays) {
        this.tradeparamInvalidWarningDays = tradeparamInvalidWarningDays;
    }

    @Override
    public String toString() {
        return "NacosConfigClient [total=" + total + ", grayRate=" + grayRate + ", pressFlag=" + pressFlag
                + ", envFlag=" + envFlag + ", industryKeyLabel=" + industryKeyLabel
                + ", tradeparamInvalidWarningType=" + tradeparamInvalidWarningType
                + ", tradeparamInvalidWarningDays=" + tradeparamInvalidWarningDays + "]";
    }
}
