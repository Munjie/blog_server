package com.munjie.blog.config;

import org.springframework.context.annotation.Configuration;
import com.munjie.blog.handler.TokemInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:48
 * @Description:
 */
@Configuration
public class WebAppConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokemInterceptor()).addPathPatterns("/**");
    }
}
