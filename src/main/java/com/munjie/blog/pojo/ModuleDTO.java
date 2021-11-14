package com.munjie.blog.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author 86158
 */
@Data
@ToString
public class ModuleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 类型
     */
    private String type;

    /**
     * 图片壁纸
     */
    private String imgUrl;

    /**
     * 排序
     */
    private Integer sort;
    /**
     * 模块id
     */
    private Integer moduleId;

    private List<ModuleDTO> children;

}
