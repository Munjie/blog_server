package com.munjie.blog.dao;

import com.munjie.blog.pojo.UserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:54
 * @Description:
 */
@Repository
public interface UserMapper {

    UserDO getUser(@Param("userName")String userName);
}
