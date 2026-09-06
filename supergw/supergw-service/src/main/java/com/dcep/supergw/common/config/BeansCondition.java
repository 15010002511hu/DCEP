package com.dcep.supergw.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Conditional;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(BeansLoadConditionConfig.class)
public @interface BeansCondition {

    /**
     * 配置 condition： config：xxxx 填写conditon.config
     */
    String name();

    //匹配条件
    String havingValue();
}
