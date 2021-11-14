package com.munjie.blog.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @author 86158
 * @Auther: munjie
 * @Date: 2/19/2021 22:12
 * @Description:
 */
@Data
@ToString
public class BlogInfoDTO {

    private String blogName;
    private Integer articleCount;
    private String avatar;
    private Integer categoryCount;
    private String github;
    private String sign;
}
