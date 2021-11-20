package com.munjie.blog.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author 86158
 */
@Data
@ToString
public class AddressDO {

    private Integer id;
    private String province;
    private String city;
    private String country;
    private String address;
    private String taskId;
    private String distance;
    private String duration;
    private Date createTime;
    private Date updateTime;
    private Double useDistance;
    private Double distanceValue;


}