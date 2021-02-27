package com.munjie.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:11
 * @Description:
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {


    private List<ArticleDO> rows;
    private Long total;
}
