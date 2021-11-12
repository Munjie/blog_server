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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:20
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String pwd;

    @Autowired
    private  Md5TokenGenerator tokenGenerator;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

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
        Jedis jedis = new Jedis(host, port);
        jedis.auth(pwd);
        String token = tokenGenerator.generate(userName, password);
        jedis.set(userName, token);
        //设置key过期时间，到期会自动删除
        jedis.expire(userName, CommonUtil.TOKEN_EXPIRE_TIME);
        //将token和username以键值对的形式存入到redis中进行双向绑定
        jedis.set(token, userName);
        jedis.expire(token, CommonUtil.TOKEN_EXPIRE_TIME);
        Long currentTime = System.currentTimeMillis();
        jedis.set(token + userName, currentTime.toString());
        //用完关闭
        jedis.close();
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
        UserDO userDO = new UserDO();
        userDO.setUserName(user.getUserName());
        userDO.setPassword(user.getPassword());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",userDO);
        jsonObject.put("token",token);
        jsonObject.put("expTime",5000);
        return Response.ok(jsonObject);

    }

    @Override
    public Response getMenus(HttpServletRequest request) {
        UserDO user = null;
        String id = "1";
        HttpSession session = request.getSession();
        if (session != null) {
            user = (UserDO) session.getAttribute("user");
        }
        List<ModuleDTO> moduleEntities = new ArrayList<>();
        if (id != null) {
            moduleEntities = roleMapper.listModuleById(id);
        }
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
