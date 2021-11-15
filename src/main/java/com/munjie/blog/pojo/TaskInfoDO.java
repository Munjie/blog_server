package com.munjie.blog.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author 86158
 */
@Data
public class TaskInfoDO {
    private Integer id;
    private String taskName;
    private String taskId;
    private String taskDescription;
    private String originAdd;
    private Date createTime;

}
