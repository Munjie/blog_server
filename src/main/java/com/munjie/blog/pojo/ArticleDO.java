package com.munjie.blog.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:10
 * @Description:
 */
@Data
@ToString
//@Document(indexName = "blog",type = "article")
//@Mapping(mappingPath = "ArticleIndex.json")
public class ArticleDO implements Serializable {

    private static final long serialVersionUID = 1L;
    //@Id
    private Integer id;

    private String articleId;

    private Integer userId;

    private String articleTitle;

    private String articleContent;

    private Integer articleViews;

    private Integer articleCommentCount;

    private Date articlePublishTime;

    private Date articleUpdateTime;

    private Date articleCreateTime;

    private Integer articleLikeCount;

    private String articleImage;

    private String articleIntro;

    private String status;

    private String articleHtml;

    private List<String> tags;
}
