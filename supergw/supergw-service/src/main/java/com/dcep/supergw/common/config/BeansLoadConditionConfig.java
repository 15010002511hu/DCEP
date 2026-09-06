package com.dcep.supergw.common.config;

import java.util.Map;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author : maxinyu
 * @version : BeansLoadConditionConfig.java v 0.1 2020-12-21
 * @description :
 */
public class BeansLoadConditionConfig implements Condition {

    private static final String NAME = "name";

    private static final String HAVINGVALUE = "havingValue";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //获取BeanCondition注解
        Map<String, Object> beanConditon = metadata.getAnnotationAttributes(BeansCondition.class.getName());
        //获取BeanCondition 注解中name 的值
        String propertynName = (String) beanConditon.get(NAME);
        //获取BeanCondition注解中的havingValue的值
        String havingValue = (String) beanConditon.get(HAVINGVALUE);

        //获取配置文件中的配置
        String propertyString = context.getEnvironment().getProperty(propertynName);
        String[] values = propertyString.split(",");

        //进行条件匹配
        for (String value : values) {
            if (value.equalsIgnoreCase(havingValue)) {
                return true;
            }
        }
        return false;
    }
}
