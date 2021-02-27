package com.munjie.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.munjie.blog.pojo.CommentDO;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:31
 * @Description:
 */
public interface CommentService {
    int addComments(CommentDO commentDO);

    JSONObject listCommentsByArticleId(String articleId);
}
