package com.munjie.blog.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class JsonRootBean {

    private Integer id;
    private String modelId;
    private String version;
    private String model;
    private List<String> textures;
    private String pose;
    private String physics;
    private Layout layout;
    private Hit_areas_custom hit_areas_custom;
    private List<Expressions> expressions;
    private Motions motions;


}