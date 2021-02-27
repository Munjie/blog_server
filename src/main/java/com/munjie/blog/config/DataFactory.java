package com.munjie.blog.config;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Auther: munjie
 * @Date: 2/23/2021 07:10
 * @Description:
 */
public class DataFactory {

    private static final String indexName = "blog";
    private static final String type = "article";
    private static final String HOST = "47.119.123.56";
    private static final int PORT = 9300;

    //读取json数据文件 list
    public static void readJsonFile(){
        //List list = new ArrayList();
        BufferedReader br;
        try{
            //创建BufferedReader(FileReader)
            br = new BufferedReader(new FileReader("D:/Work_Space/es-index/src/test.txt"));
            String line = null;
            while ((line=br.readLine())!= null ){
                JSONObject dataJson = new JSONObject(line);
                //读一行处理一行 (获得需要的索引字段)
//                CrmTeacherObject teacherObject = new CrmTeacherObject(
//                        dataJson.get("teacherId")== JSONObject.NULL ? 0:dataJson.getLong("teacherId"),
//                        dataJson.get("realName") == JSONObject.NULL ? "":dataJson.getString("realName"),
//                        dataJson.get("mobile") == JSONObject.NULL ? "":dataJson.get("mobile").toString()
//                );
                //交给JsonUtil处理，成创建索引需要的字符串
                String JsonString = JsonUtil.objToJsonData(null);
                //创建索引
                ElasticSearchIndex esi = new ElasticSearchIndex();
                //得到ES连接 client
                TransportClient client = esi.client();
                //创建索引
                IndexResponse indexResponse = esi.doCreateIndexResponse( client,indexName ,type ,JsonString);
                //System.out.println(indexResponse.getIndex());

                //查询索引
                //GetResponse getResponse = esi.getIndexResponse(client);
                //System.out.println(getResponse.getIndex());

                //关闭ES连接
                client.close();
            }
            //关闭 BufferedReader
            br.close();
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        readJsonFile();
    }
}
