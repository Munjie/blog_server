package com.munjie.blog;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

/**
 * @Auther: munjie
 * @Date: 2/4/2021 21:30
 * @Description:
 */
@Slf4j
@EnableAsync
@MapperScan({"com.munjie.blog.dao"})
@SpringBootApplication
@EnableTransactionManagement
@EnableWebSocketMessageBroker
@EnableScheduling
public class BlogApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(BlogApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("Server has been started successfully...");
    }

}
