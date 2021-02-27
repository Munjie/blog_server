package com.munjie.blog.config;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Auther: munjie
 * @Date: 2/23/2021 07:13
 * @Description:
 */
public class ElasticSearchIndex {

    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port}")
    private int esPort;

    @Value("${elasticsearch.clustername}")
    private String esClusterName;

    //获得client 连接
    public TransportClient client() {
        // 9300是es的tcp服务端口
        TransportAddress node = null;
        try{
            node = new TransportAddress(
                    InetAddress.getByName(esHost),
                    esPort);
        }catch(UnknownHostException e){
            // 打印错误信息，并且抛出错误异常
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

    //关闭client 连接
    public void close(Client client) {
        if (client != null) {
            client.close();
        }
    }

    //创建索引
    public IndexResponse doCreateIndexResponse(Client client, String indexName, String type, String json) {
        //Client client = getClient(HOST,PORT);
        IndexResponse response = client.prepareIndex(indexName, type)
                .setSource(json)
                .execute()
                .actionGet();
        return response;
    }


    //查询索引
    public SearchResponse doSerch(Client client, String indexName, String type){
        SearchResponse response = client.prepareSearch(indexName).setTypes(type).execute().actionGet();
        return response;
    }
}
