package com.munjie.blog.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:12
 * @Description:
 */
@Data
@ToString
public class CommentDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private  Integer commentId;
    private String articleId;
    private  Integer parentId;
    private  Integer replyId;
    private  String userName;
    private  String email;
    private  String content;
    private  Integer isAuthor;
    private String createTime;
    private String gravatar;
    private List<ReplyDO> children;
}
