package com.munjie.blog.config;

import com.munjie.blog.pojo.ArticleDO;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * @Auther: munjie
 * @Date: 2/23/2021 07:17
 * @Description:
 */
public class JsonUtil {

    public static String objToJsonData(ArticleDO articleDO){
        String jsonData=null;
        try{
            XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
            jsonBuilder.startObject()
                    .field("articleId",articleDO.getArticleId())
                    .field("articleTitle",articleDO.getArticleTitle())
                    .field("articleContent",articleDO.getArticleContent())
                    .endObject();
            jsonData = jsonBuilder.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return jsonData;
    }
}
