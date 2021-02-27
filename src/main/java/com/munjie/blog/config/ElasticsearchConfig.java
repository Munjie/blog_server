package com.munjie.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:38
 * @Description:
 */
@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.munjie.blog.dao")
public class ElasticsearchConfig {
    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port}")
    private int esPort;

    @Value("${elasticsearch.clustername}")
    private String esClusterName;

    @Value("${elasticsearch.poolsize}")
    private Integer threadPoolSearchSize;


    @Bean
    public TransportClient client() {
        // 9300是es的tcp服务端口
        TransportAddress node = null;
        try{
            node = new TransportAddress(
                    InetAddress.getByName(esHost),
                    esPort);
        }catch(UnknownHostException e){
            // 打印错误信息，并且抛出错误异常
            log.error(e.getMessage());
        }
        // 设置es节点的配置信息
        Settings settings = Settings.builder()
                .put("cluster.name", esClusterName)
                .build();
        // 实例化es的客户端对象
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(node);
        return client;
    }




    @Bean(name="elasticsearchTemplate")
    public ElasticsearchOperations elasticsearchTemplateCustom() throws Exception {
        ElasticsearchTemplate elasticsearchTemplate;
        try {
            elasticsearchTemplate = new ElasticsearchTemplate(client());
            return elasticsearchTemplate;
        } catch (Exception e) {
            log.error("初始化ElasticsearchTemplate失败");
            return new ElasticsearchTemplate(client());
        }
    }


    @Bean
    public RestHighLevelClient restHighLevelClient() {
        // 发现本地的服务 多个可配置集群
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(esHost, 9200, "http")));
        return client;
    }

}
