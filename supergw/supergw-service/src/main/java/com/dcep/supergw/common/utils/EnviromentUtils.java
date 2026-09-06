package com.dcep.supergw.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnviromentUtils {

    private static Environment environment;

    public static String get(String key) {
        return environment.getProperty(key);
    }

    public static <T> T get(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        EnviromentUtils.environment = environment;
    }
}
