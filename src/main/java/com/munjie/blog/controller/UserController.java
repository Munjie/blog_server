package com.munjie.blog.controller;

import com.munjie.blog.pojo.Response;
import com.munjie.blog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Validated
@Api(tags = "用户类相关API")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Response login(String username, String password) {
        return userService.login(username, password);

    }


    @ApiOperation("获取用户登录菜单")
    @GetMapping("/getMenus/{userNo}")
    public Response getMenus(HttpServletRequest request, @PathVariable("userNo") String userNo) {
        return userService.getMenus(request, userNo);
    }

}
