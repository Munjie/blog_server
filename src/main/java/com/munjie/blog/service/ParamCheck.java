package com.munjie.blog.service;

import java.lang.annotation.*;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:21
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)			//生命周期在运行期
@Target({ElementType.PARAMETER})			// 在参数上使用
@Documented            // 保存在JavaDoc中
public @interface ParamCheck {
    boolean notNull() default true;
}
