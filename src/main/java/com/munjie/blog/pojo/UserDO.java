package com.munjie.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:14
 * @Description:
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String userName;
    private String userNo;
    private String password;
    private String email;
    private Integer roleId;
    private Date createTime;


}
