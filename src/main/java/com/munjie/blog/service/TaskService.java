package com.munjie.blog.service;

import com.github.pagehelper.PageInfo;
import com.munjie.blog.pojo.Response;
import com.munjie.blog.pojo.TaskInfoDO;
import com.munjie.blog.pojo.TaskInfoDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 86158
 */
public interface TaskService {


    Response createTask(TaskInfoDO info, MultipartFile file, HttpServletRequest request);


    PageInfo<TaskInfoDO> page(Map<String, Object> map);


    Response deleteTask(String taskId) throws Exception;

    TaskInfoDTO listTask(Integer pageSize, Integer pageNo);

    void exportExcel(String taskId, HttpServletResponse response) throws Exception;


}
