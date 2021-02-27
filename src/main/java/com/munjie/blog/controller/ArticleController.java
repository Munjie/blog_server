package com.munjie.blog.controller;

import com.munjie.blog.pojo.Response;
import com.munjie.blog.service.ArticleService;
import com.munjie.blog.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:50
 * @Description:
 */
@Validated
@Api(tags = "文章api")
@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {

    private ArticleService articleService;
    private TagService tagService;

    public ArticleController(ArticleService articleService, TagService tagService) {
        this.articleService = articleService;
        this.tagService = tagService;
    }

    @ApiOperation("文章首页分页")
    @GetMapping("/listArticles")
    public Response listArticles(@ApiParam(value = "分页每页展示数量", required = true) @RequestParam("pageSize") Integer pageSize,
                                 @ApiParam(value = "分页页码", required = true) @RequestParam("pageNo") Integer pageNo) {
        return Response.ok(articleService.listArticles(pageSize, pageNo));
    }


    @ApiOperation("文章详细信息")
    @GetMapping("/getArticleDetailById/{articleId}")
    public Response getArticleDetailById(@PathVariable("articleId") String articleId) {
        Map map = new HashMap();
        map.put("article",articleService.getArticleDetailById(articleId));
        return Response.ok(map);
    }

    @ApiOperation("文章评论列表")
    @GetMapping("/listComments")
    public Response listComments(String articleId) {
        Map map = new HashMap();
        map.put("article",articleService.getArticleDetailById(articleId));
        return Response.ok(map);
    }


    @ApiOperation("文章编辑根据id查询")
    @GetMapping("/info")
    public Response info(@ApiParam(value = "文章id", required = true) @RequestParam("id") String id) {
        Map map = new HashMap();
        map.put("article",articleService.getArticleDetailById(id));
        return Response.ok(map);
    }


    @ApiOperation("文章搜索")
    @GetMapping("/listSearch")
    public Response listSearch(@ApiParam(value = "分页每页展示数量", required = true) @RequestParam("pageSize") Integer pageSize,
                               @ApiParam(value = "分页页码", required = true) @RequestParam("pageNo") Integer pageNo,@ApiParam(value = "分页页码", required = true) @RequestParam("content") String content) {
        return Response.ok(articleService.listSearch(pageSize,pageNo,content));
    }


    @ApiOperation("文章搜索")
    @GetMapping("/tagArticles")
    public Response tagArticles(@ApiParam(value = "分页每页展示数量", required = true) @RequestParam("pageSize") Integer pageSize,
                                @ApiParam(value = "分页页码", required = true) @RequestParam("pageNo") Integer pageNo,@ApiParam(value = "分页页码", required = true) @RequestParam("tagName") String tagName) {
        return Response.ok(articleService.tagArticles(pageSize,pageNo,tagName));
    }


    @ApiOperation("标签列表")
    @GetMapping("/listAllTags")
    public Response listAllTags() {

        return Response.ok(tagService.listAllTags());
    }



    @ApiOperation("编辑标签")
    @GetMapping("/listEditTags")
    public Response listEditTags() {

        return Response.ok(tagService.listEditTags());
    }



}
