package com.munjie.blog.dao;

import com.munjie.blog.pojo.ArticleDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:52
 * @Description:
 */
@Repository
public interface ArticleMapper {


    List<ArticleDO> listArticle();


    List<ArticleDO> listBack();

    ArticleDO getArticleDetailById(@Param("articleId")String articleId);

    int saveArticle(ArticleDO articleDO);


    int deleteArticle(@Param("articleId") String articleId);

    int updateArticleView(@Param("articleId")String articleId);


    List<ArticleDO> listArticleByTag(@Param("tagName")String tagName);

}
