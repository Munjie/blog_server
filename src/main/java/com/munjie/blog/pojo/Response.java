package com.munjie.blog.pojo;

import com.munjie.blog.enums.ExceptionEnum;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:13
 * @Description:
 */
public class Response  extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    public Response() {
        this.put((String)"code", 200);
        this.put((String)"reason", "SUCCESS");
        this.put((String)"message", (Object)null);
    }

    public static Response error() {
        return error(1, "操作失败");
    }

    public static Response error(String msg) {
        return error(500, msg);
    }

    public static Response error(Object msg) {
        Response r = new Response();
        r.put((String)"code", 500);
        r.put("data", msg);
        r.put((String)"reason", (Object)null);
        return r;
    }

    public static Response error(ExceptionEnum exceptionEnum) {
        Response r = new Response();
        r.put((String)"code", exceptionEnum.getCode());
        r.put((String)"data", exceptionEnum.getMsg());
        r.put((String)"reason", (Object)null);
        return r;
    }

    public static Response error(int code, String msg) {
        Response r = new Response();
        r.put((String)"code", code);
        r.put((String)"message", msg);
        r.put((String)"reason", (Object)null);
        return r;
    }

    public static Response ok(String msg) {
        Response r = new Response();
        r.put((String)"message", msg);
        return r;
    }

    public static Response ok(Object object) {
        Response r = new Response();
        r.put("data", object);
        return r;
    }

    public static Response ok() {
        Response r = new Response();
        return r;
    }

    public static Response ok(Object object, String message) {
        Response r = new Response();
        r.put("data", object);
        r.put((String)"message", message);
        return r;
    }

    @Override
    public Response put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
