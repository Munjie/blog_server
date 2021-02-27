package com.munjie.blog.service;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:32
 * @Description:
 */
public interface TokenGenerator {
    String generate(String... strings);
}
