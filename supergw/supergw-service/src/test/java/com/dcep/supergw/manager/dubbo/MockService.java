package com.dcep.supergw.manager.dubbo;

import com.dcep.clearing.api.ClearingService;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.TestMsgUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class MockService implements ApplicationListener<ApplicationEvent> {

    private final static String ClearingService = "com.dcep.clearing.api.ClearingService";

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            ConfigurableApplicationContext context = (ConfigurableApplicationContext) ((ContextRefreshedEvent) event)
                .getApplicationContext();
            DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getBeanFactory();
            //替换ClearingService服务
            replaceBean(ClearingService, new MockClearingService(), factory);
        }
    }

    private void replaceBean(String name, Object mock, DefaultListableBeanFactory factory) {
        BeanDefinition definition = factory.getBeanDefinition(name);
        factory.removeBeanDefinition(name);
        definition.setBeanClassName(mock.getClass().getName());
        factory.registerBeanDefinition(name, definition);
        factory.registerSingleton(name, mock);
    }

    class MockClearingService implements ClearingService {

        @Override
        public Response<GwDTO> prepare(EnvelopeDTO<GwDTO> req) throws DcepException {
            String xml = TestMsgUtils.dcep_201();
            return new Response(SoapUtils.toDto(SoapUtils.getSoapHeaderBean(xml.getBytes()), xml.getBytes()));
        }

        @Override
        public Response<GwDTO> finish(EnvelopeDTO<GwDTO> req) throws DcepException {
            String xml = TestMsgUtils.dcep_202();
            return new Response(SoapUtils.toDto(SoapUtils.getSoapHeaderBean(xml.getBytes()), xml.getBytes()));
        }
    }
}
