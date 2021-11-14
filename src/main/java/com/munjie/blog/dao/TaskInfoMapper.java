package com.munjie.blog.dao;

import com.munjie.blog.pojo.TaskInfoDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskInfoMapper {



    int deleteById(Integer taskId);

    int insert(TaskInfoDO taskInfoDO);

    List<TaskInfoDO> listTask();




}
