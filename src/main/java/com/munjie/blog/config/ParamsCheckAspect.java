package com.munjie.blog.config;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:46
 * @Description:
 */
/*@Component
@Aspect*/
public class ParamsCheckAspect {

/*    @Pointcut("execution(* com.munjie.blog.controller.*.*(..))")
    public void checkParams() {

    }
    // 执行过程是 around - before - 逻辑代码 - after - afterReturning - （如果出现异常）afterThrowing
    @Around("checkParams()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 获取切点的署名
        Method method = signature.getMethod();  // 得到拦截的方法
        //获取方法参数注解，返回二维数组是因为某些参数可能存在多个注解  --- 如果该方法参数都没有注解则直接跳过
        java.lang.annotation.Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return joinPoint.proceed();
        }
        // 获取参数名
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();    // 获取参数值
        //获取方法参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[0].length; j++) {
                // 如果参数注解不为空，且参数注解是ParamCheck的派生类，且notNull是true，且 参数值为null 抛出空异常
                if (parameterAnnotations[i][j] != null && parameterAnnotations[i][j] instanceof ParamCheck
                        && ((ParamCheck) parameterAnnotations[i][j]).notNull() && args[i] == null) {
                    throw new ParamsException(parameterNames[i], parameterTypes[i].getTypeName());
                }
            }
        }

        return joinPoint.proceed();
    }

    @Before("checkParams()")
    public void before(){
        System.out.println("执行了before");
    }*/
}
