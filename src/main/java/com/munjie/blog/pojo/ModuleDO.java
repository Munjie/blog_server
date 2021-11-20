package com.munjie.blog.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author 86158
 */
@Data
@ToString
public class ModuleDO  {

    private Integer id;

    /**
     * 地址
     */
    private String url;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片壁纸
     */
    private String imgUrl;

    private String parent;

    /**
     * 排序
     */
    private Integer sort;
    /**
     * 模块id
     */
    private String moduleId;
    private String createDate;
    private Date createTime;
    private Date updateTime;

}
