package com.dcep.supergw.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author : maxinyu
 * @version : LogAspect.java v 0.1 2020-12-03
 * @description :
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(com.dcep.supergw.common.aop.LogPointCut))")
    public void logPointcut() {

    }

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint pj) throws Throwable {
        log.info("=======方法开始执行=======");
        for (Object o : pj.getArgs()) {
            log.info("方法的入参值为---->{}", o);
        }
        try {
            Object response = pj.proceed();
            log.info("方法返回结果为---->{}", response.toString());
            return response;
        } catch (Throwable throwable) {
            log.info("切面执行异常:{}", throwable);
            throw throwable;
        }
    }
}
