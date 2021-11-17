package com.munjie.blog.service;

import com.munjie.blog.pojo.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:33
 * @Description:
 */
public interface UserService {
    String setRedisData(String userName, String password);

    Response login(String userName, String password);

    Response getMenus(HttpServletRequest request,String userNo);
}
