package com.munjie.blog.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.List;
@Data
@ToString
public class Hit_areas_custom {

    private List<Double> head_x;
    private List<Double> head_y;
    private List<Double> body_x;
    private List<Double> body_y;

}
