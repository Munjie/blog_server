package com.munjie.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @Auther: munjie @Date: 2/25/2021 22:44 @Description:
 * 配置WebSocket消息代理端点，即stomp服务端 * 为了连接安全，setAllowedOrigins设置的允许连接的源地址 * 如果在非这个配置的地址下发起连接会报403 *
 * 进一步还可以使用addInterceptors设置拦截器，来做相关的鉴权操作
 */
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("http://localhost:8088")
                .withSockJS();
    }
}
