package com.munjie.blog.config;

import com.munjie.blog.utils.AopLogAspectUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:35
 * @Description:
 */
@Aspect
@Component
public class AopLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopLogAspect.class);


    /**
     * 定义切入点，切入点为com.example.aop下的所有函数
     */
    @Pointcut("execution(* com.munjie.blog.controller.*.*(..))")
    public void aopLog() {
    }

    /**
     * 前置通知：在连接点之前执行的通知
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("aopLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        LOGGER.info("请求前置通知开始...");
        // 接收到请求，记录请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        LOGGER.info("请求URL ={}", request.getRequestURL().toString());
        LOGGER.info("请求方式 ={}", request.getMethod());
        //  LOGGER.info("请求IP ={}", request.getRemoteAddr());
        LOGGER.info("请求类名={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        Signature signature = joinPoint.getSignature();
        LOGGER.info("方法名={}", signature.getName());
        Object[] args = joinPoint.getArgs();
        String classType = joinPoint.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);
        String clazzName = clazz.getName();
        String methodName = joinPoint.getSignature().getName(); // 获取方法名称
        // 获取参数名称和值
        StringBuffer sb = AopLogAspectUtil.getNameAndArgs(this.getClass(), clazzName, methodName, args);
        LOGGER.info("请求报文={}", sb);
    }



    @AfterReturning(returning = "ret", pointcut = "aopLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        if (ret != null) {
            LOGGER.info("返回响应结果={}", ret);
        }
    }
}
