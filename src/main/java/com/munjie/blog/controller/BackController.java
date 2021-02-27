package com.munjie.blog.controller;

import com.munjie.blog.pojo.ArticleDO;
import com.munjie.blog.pojo.Response;
import com.munjie.blog.service.ArticleService;
import com.munjie.blog.service.QiniuService;
import com.munjie.blog.service.TagService;
import com.munjie.blog.service.UserService;
import com.munjie.blog.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:51
 * @Description:
 */
@Validated
@Api(tags = "文章api")
@RestController
@RequestMapping("/back")
@CrossOrigin
public class BackController {

    private ArticleService articleService;

    public BackController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Autowired
    private UserService userService;
    @Autowired
    private QiniuService qiniuService;

    @Autowired
    private TagService tagService;


    @ApiOperation("文章后台")
    @GetMapping("/home")
    public Response home() {
        return Response.ok("");
    }


    @ApiOperation("日志")
    @GetMapping("/log")
    public Response log() {
        return Response.ok("");
    }



    @ApiOperation("登录")
    @PostMapping("/login")
    public Response login(String username, String password) {
        return userService.login(username,password);

    }

    @ApiOperation("编辑")
    @PostMapping("/editArticle")
    public Response editArticle(String articleId) {
        return Response.ok("success");
    }


    @ApiOperation("保存文章")
    @PostMapping("/save")
    public Response save(ArticleDO articleDO) {
        articleService.saveArticle(articleDO);
        return Response.ok("success");
    }

    @ApiOperation("删除文章")
    @GetMapping("/deleteArticle/{articleId}")
    public Response deleteArticle(@PathVariable("articleId")String   articleId) {
        articleService.deleteArticle(articleId);
        return Response.ok("success");
    }



    @ApiOperation("发布文章")
    @PostMapping("/publish")
    public Response publish(ArticleDO articleDO) {
        articleService.publishArticle(articleDO);
        return Response.ok("success");
    }


    @ApiOperation("后台文章首页分页")
    @GetMapping("/listArticles")
    public Response listArticles(@ApiParam(value = "分页每页展示数量", required = true) @RequestParam("pageSize") Integer pageSize,
                                 @ApiParam(value = "分页页码", required = true) @RequestParam("pageNo") Integer pageNo) {
        return Response.ok(articleService.listBack(pageSize, pageNo));
    }

    /** 文章编辑本地上传图片 */
    @PostMapping("/uploadImage")
    public Response uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
            String fileUrl = FileUtil.upload(file);
            return Response.ok(fileUrl);
    }

    @ApiOperation("删除文章")
    @GetMapping("/getQiniuToken")
    public Response getQiniuToken() {
        String token = qiniuService.getToken();
        return Response.ok(token);
    }

    @ApiOperation("新增标签")
    @PostMapping("/addTag")
    public Response save(String tag) {
        int i = tagService.addTag(tag);
        return Response.ok("success");
    }
}
