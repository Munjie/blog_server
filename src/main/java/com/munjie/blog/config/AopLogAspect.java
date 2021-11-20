package com.munjie.blog.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class AopLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopLogAspect.class);

    /**
     * 切入点
     * 匹配top.alanlee.template.controller包及其子包下的所有类的所有方法
     */
    @Pointcut("execution(* com.munjie.blog.controller.*.*(..))")
    public void pointCut(){

    }

    /**
     * 前置通知，目标方法调用前被调用
     */
    @Before("pointCut()")
    public void beforeAdvice(JoinPoint joinPoint){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        LOGGER.info("----------- 前置通知 -----------");
        Signature signature = joinPoint.getSignature();
        LOGGER.info("请求IP：" + request.getRemoteAddr());
        LOGGER.info("方法签名：" + signature);
        LOGGER.info("方法名：" + signature.getName());
        Object[] args = joinPoint.getArgs();
        LOGGER.info("请求参数：" + Arrays.asList(args));

    }

    /**
     * 最终通知，目标方法执行完之后执行
     */
    @After("pointCut()")
    public void afterAdvice(JoinPoint jp){
        LOGGER.info("----------- 最终通知 -----------"+ jp);

    }

    /**
     * 后置返回通知
     * 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     * 如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning 只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行
     * @param object
     */
    @AfterReturning(value = "execution(* com.munjie.blog.controller..*.*(..))", returning = "object")
    public void afterReturningAdvice(Object object){
        LOGGER.info("----------- 后置返回通知 -----------");
        LOGGER.info("后置返回通知的返回值：" + object);
    }

    /**
     * 后置异常通知
     * 定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     * throwing 只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "pointCut()", throwing = "e")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception e){
        LOGGER.info("----------- 后置异常通知 -----------");
    }

    /**
     * 环绕通知
     * 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     * @param proceedingJoinPoint
     */
    @Around("execution(* com.munjie.blog.controller.ArticleController.listArticles(..))")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
        LOGGER.info("----------- 环绕通知 -----------");
        LOGGER.info("环绕通知的目标方法名：" + proceedingJoinPoint.getSignature().getName());

        try {
            Object proceed = proceedingJoinPoint.proceed();
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }finally {
            LOGGER.info("---------- 环绕通知结束 -------------");
        }
        return null;
    }

}