package com.munjie.blog.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.munjie.blog.pojo.BlogInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:51
 * @Description:
 */
@Validated
@Api(tags = "博客信息API")
@RestController
@RequestMapping("/blogInfo")
@CrossOrigin
public class BlogInfoController {
    @ApiOperation("文章首页分页")
    @GetMapping("/about")
    public R getAbout() {

        BlogInfoDTO blogInfo = new BlogInfoDTO();
        blogInfo.setBlogName("Munjie");
        blogInfo.setArticleCount(12);
        blogInfo.setAvatar("");
        blogInfo.setCategoryCount(20);
        blogInfo.setGithub("wwww");
        blogInfo.setSign("kkk");
        return R.ok(blogInfo);
    }
}
