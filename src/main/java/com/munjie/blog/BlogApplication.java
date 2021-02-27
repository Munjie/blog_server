package com.munjie.blog;

import com.munjie.blog.config.LoggerQueue;
import com.munjie.blog.pojo.LoggerMessage;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    /**
     * 推送日志到/topic/pullLogger
     */
    @PostConstruct
    public void pushLogger(){
        ExecutorService executorService= Executors.newFixedThreadPool(2);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        LoggerMessage log = LoggerQueue.getInstance().poll();
                        if(log!=null){
                            if(messagingTemplate!=null) {
                                messagingTemplate.convertAndSend("/topic/pullLogger",log);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        executorService.submit(runnable);
        executorService.submit(runnable);
    }
}
