package com.munjie.blog.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:12
 * @Description:
 */
@Data
@ToString
public class BlogInfoDTO {

    private String blogName;
    private int articleCount;
    private String avatar;
    private int categoryCount;
    private String github;
    private String sign;
}
