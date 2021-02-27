package com.munjie.blog.controller;

import com.munjie.blog.pojo.CommentDO;
import com.munjie.blog.pojo.Response;
import com.munjie.blog.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:52
 * @Description:
 */
@Validated
@Api(tags = "评论api")
@RestController
@RequestMapping("/comments")
@CrossOrigin
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation("添加评论")
    @PostMapping("/addComments")
    public Response addComments(CommentDO commentDO) {
        commentService.addComments(commentDO);
        return Response.ok("success");
    }

    @ApiOperation("根据文章id查询评论")
    @GetMapping("/listCommentsByArticleId/{articleId}")
    public Response listCommentsByArticleId(@PathVariable("articleId") String articleId) {
        return Response.ok(commentService.listCommentsByArticleId(articleId));
    }
}
