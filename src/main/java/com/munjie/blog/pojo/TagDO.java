package com.munjie.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:14
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TagDO {

    private int id;
    private int total;
    private String tagName;
    private String articleId;
}
