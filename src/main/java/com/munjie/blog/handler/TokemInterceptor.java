package com.munjie.blog.handler;

import com.alibaba.fastjson.JSONObject;
import com.munjie.blog.service.AuthToken;
import com.munjie.blog.utils.CommonUtil;
import com.munjie.blog.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:09
 * @Description:
 */
public class TokemInterceptor implements HandlerInterceptor {


    Logger log = LoggerFactory.getLogger(TokemInterceptor.class);

    //存放鉴权信息的Header名称，默认是Authorization
    private String Authorization = "Authorization";

    //鉴权失败后返回的HTTP错误码，默认为401
    private int unauthorizedErrorCode = HttpServletResponse.SC_UNAUTHORIZED;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 存放用户名称和对应的key
     */
    public static final String USER_KEY = "USER_KEY";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //验证token
        if (method.getAnnotation(AuthToken.class) != null || handlerMethod.getBeanType().getAnnotation(AuthToken.class) != null) {
            String token = request.getParameter(Authorization);
            log.info("获取到的token为: {} ", token);
            //此处主要设置你的redis的ip和端口号，我的redis是在本地
            String username = null;
            if (token != null && token.length() != 0) {
                //从redis中根据键token来获取绑定的username
                username = redisUtil.get(token).toString();
                log.info("从redis中获取的用户名称为: {}", username);
            }
            //判断username不为空的时候
            if (username != null && !username.trim().equals("")) {
                String startBirthTime = redisUtil.get(token + username).toString();
                log.info("生成token的时间为: {}", startBirthTime);
                Long time = System.currentTimeMillis() - Long.valueOf(startBirthTime);
                log.info("token存在时间为 : {} ms", time);
                //重新设置Redis中的token过期时间
                if (time > CommonUtil.TOKEN_RESET_TIME) {
                    redisUtil.expire(username, CommonUtil.TOKEN_EXPIRE_TIME);
                    redisUtil.expire(token, CommonUtil.TOKEN_EXPIRE_TIME);
                    log.info("重置成功!");
                    Long newBirthTime = System.currentTimeMillis();
                    redisUtil.set(token + username, newBirthTime.toString());
                }

                //关闭资源
                request.setAttribute(USER_KEY, username);
                return true;
            } else {
                JSONObject jsonObject = new JSONObject();

                PrintWriter out = null;
                try {
                    response.setStatus(unauthorizedErrorCode);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    jsonObject.put("code", ((HttpServletResponse) response).getStatus());
                    //鉴权失败后返回的错误信息，默认为401 unauthorized
                    jsonObject.put("message", HttpStatus.UNAUTHORIZED);
                    out = response.getWriter();
                    out.println(jsonObject);

                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != out) {
                        out.flush();
                        out.close();
                    }
                }

            }

        }

        request.setAttribute(USER_KEY, null);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
