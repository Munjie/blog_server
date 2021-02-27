package com.munjie.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:13
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageResult<T> {
    private Long total;// 总条数
    private Long totalPage;// 总页数
    private List<T> items;// 当前页数据
    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }
}
