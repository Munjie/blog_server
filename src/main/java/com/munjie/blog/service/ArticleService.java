package com.munjie.blog.service;

import com.munjie.blog.pojo.ArticleDO;
import com.munjie.blog.pojo.ArticleDTO;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:17
 * @Description:
 */
public interface ArticleService {
    ArticleDTO listArticles(Integer pageSize, Integer pageNo);

    int saveArticle(ArticleDO articleDO);

    ArticleDO getArticleDetailById(String articleId);

    int deleteArticle(String articleId);

    ArticleDTO listSearch(Integer pageSize, Integer pageNo, String content);

    int publishArticle(ArticleDO articleDO);

    ArticleDTO listBack(Integer pageSize, Integer pageNo);

    ArticleDTO tagArticles(Integer pageSize, Integer pageNo, String tagName);

    int updateStatusById(String articleId,String status);
}
