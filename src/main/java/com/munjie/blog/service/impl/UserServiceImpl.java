package com.munjie.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.munjie.blog.dao.RoleMapper;
import com.munjie.blog.dao.UserMapper;
import com.munjie.blog.pojo.ModuleDTO;
import com.munjie.blog.pojo.Response;
import com.munjie.blog.pojo.UserDO;
import com.munjie.blog.service.UserService;
import com.munjie.blog.utils.CommonUtil;
import com.munjie.blog.utils.RedisUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:20
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private  Md5TokenGenerator tokenGenerator;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RedisUtil redisUtil;

    public static String getToken(String userName, String password) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 1000;//一小时有效时间
        Date end = new Date(currentTime);
        return JWT.create().withAudience(userName).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(password));
    }

    @Override
    public String setRedisData(String userName, String password) {
        //此处主要设置你的redis的ip和端口号，我的redis是在本地
        String token = tokenGenerator.generate(userName, password);
        redisUtil.set(userName, token);
        //设置key过期时间，到期会自动删除
        redisUtil.expire(userName, CommonUtil.TOKEN_EXPIRE_TIME);
        //将token和username以键值对的形式存入到redis中进行双向绑定
        redisUtil.set(token, userName);
        redisUtil.expire(token, CommonUtil.TOKEN_EXPIRE_TIME);
        Long currentTime = System.currentTimeMillis();
        redisUtil.set(token + userName, currentTime.toString());
        return token;
    }

    @Override
    public Response login(String userName, String password) {
        UserDO user = userMapper.getUser(userName);
        if (user == null) {
            return Response.error("用户不存在！");
        } else if (!password.equals(user.getPassword())) {
            return Response.error("用户密码错误！");
        }
        String token = setRedisData(userName, password);
        // String token = getToken(userName, password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",user);
        jsonObject.put("token",token);
        jsonObject.put("expTime",5000);
        return Response.ok(jsonObject);

    }

    @Override
    public Response getMenus(HttpServletRequest request,String userNo) {
        List<ModuleDTO> moduleEntities = roleMapper.listModuleById(userNo);
        List<String> list = roleMapper.listParent();
        for (ModuleDTO mo:moduleEntities) {
            if (mo != null && CollectionUtils.isNotEmpty(list)) {
                for (String str:list) {
                    if (str.equals(mo.getName())) {
                        List<ModuleDTO> moduleEntities1 = roleMapper.listModuleByParent(mo.getName());
                        mo.setChildren(moduleEntities1);
                    }
                }
            }
        }
        return Response.ok(moduleEntities);
    }
}
