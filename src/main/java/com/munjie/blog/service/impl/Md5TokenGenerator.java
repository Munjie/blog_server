package com.munjie.blog.service.impl;

import com.munjie.blog.service.TokenGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:18
 * @Description:
 */
@Service
public class Md5TokenGenerator implements TokenGenerator {

    @Override
    public String generate(String... strings) {
        long timestamp = System.currentTimeMillis();
        String tokenMeta = "";
        for (String s : strings) {
            tokenMeta = tokenMeta + s;
        }
        tokenMeta = tokenMeta + timestamp;
        String token = DigestUtils.md5DigestAsHex(tokenMeta.getBytes());
        return token;
    }
}
