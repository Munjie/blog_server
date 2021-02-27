package com.munjie.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.munjie.blog.dao.CommentMapper;
import com.munjie.blog.pojo.CommentDO;
import com.munjie.blog.pojo.ReplyDO;
import com.munjie.blog.service.CommentService;
import com.munjie.blog.utils.CommonUtil;
import com.munjie.blog.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:18
 * @Description:
 */
@Service
@SuppressWarnings("all")
public class CommentServiceImpl implements CommentService {


    private CommentMapper commentMapper;

    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public int addComments(CommentDO commentDO) {
        if (commentDO.getReplyId() != 0) {
            commentDO.setParentId(commentDO.getReplyId());
        }
        if (StringUtils.isNotEmpty(commentDO.getEmail())){
            String s = CommonUtil.gravatarImg(commentDO.getEmail());
            commentDO.setGravatar(s);
        }
        commentDO.setCreateTime(DateUtil.getNowDate());
        return commentMapper.addComments(commentDO);

    }

    @Override
    public JSONObject listCommentsByArticleId(String articleId) {

        JSONObject jsonObject = new JSONObject();
        List<CommentDO> result = getAllCommentByBlogId(articleId);
        /*List<CommentDO> all = commentMapper.listCommentsByArticleId(articleId);
        Map<Integer, CommentDO> map = new HashMap<>();
        List<CommentDO> result = new ArrayList<>();
        for (CommentDO c : all) {
            if (c.getParentId() == null) {
                result.add(c);
            }
            map.put(c.getCommentId(), c);
        }
        for (CommentDO c : all) {
            if (c.getParentId() != null) {
                CommentDO parent = map.get(c.getParentId());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(c);
            }
        }*/

        jsonObject.put("count",result.size());
        jsonObject.put("list",result);
        return jsonObject;
    }

    public List<CommentDO> getAllCommentByBlogId(String articleId) {
        List<CommentDO> result = new ArrayList<>();
        // 查找文章下所有的父级评论
        List<Integer> integers = commentMapper.getCommentIdByBlogId(articleId);
        for (Integer commentId : integers) {
            CommentDO commentDO = commentMapper.selectCommentById(commentId);
            //查找所有父级评论下的子评论
            List<ReplyDO> replyDOS = commentMapper.listCommentsByParentId(commentDO.getCommentId());
            commentDO.setChildren(replyDOS);
            result.add(commentDO);
        }
        return result;
    }

}
