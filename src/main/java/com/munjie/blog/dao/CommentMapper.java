package com.munjie.blog.dao;

import com.munjie.blog.pojo.CommentDO;
import com.munjie.blog.pojo.ReplyDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:53
 * @Description:
 */
@Repository
public interface CommentMapper {

    /**
     *
     * @param articleId
     * @return
     */
    List<CommentDO> listCommentsByArticleId(String articleId);


    /**
     *
     * @param commentDO
     * @return
     */
    int addComments(CommentDO commentDO);


    /**
     *
     * @param parentId
     * @return
     */
    List<ReplyDO> listCommentsByParentId(Integer parentId);


    /**
     * @description //TODO
     * @param:
     * @return:
     * @date: 2/25/2021 7:40 PM
     */
    List<Integer> getCommentIdByBlogId(String articleId);

    /**
     * @description //TODO
     * @param:
     * @return:
     * @date: 2/25/2021 7:40 PM
     */
    CommentDO  selectCommentById(Integer commentId);
}
