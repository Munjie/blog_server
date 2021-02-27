package com.munjie.blog.dao;

import com.munjie.blog.pojo.ArticleDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:29
 * @Description:
 */
public interface ArticleRepository extends ElasticsearchRepository<ArticleDO, String> {

}
