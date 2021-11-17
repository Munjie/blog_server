package com.munjie.blog.controller;


import com.munjie.blog.dao.AddressMapper;
import com.munjie.blog.pojo.Response;
import com.munjie.blog.pojo.TaskInfoDO;
import com.munjie.blog.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Validated
@Api(tags = "处理excel任务接口")
@RestController
@RequestMapping("/task")
@CrossOrigin
public class TaskConller {

    @Autowired
    private TaskService taskService;

    @Autowired
    private AddressMapper addressMapper;

    @PostMapping("/createTask")
    @ApiOperation("处理excel")
    public Response createTask(@RequestPart TaskInfoDO info, MultipartFile file, HttpServletRequest request) {
        return taskService.createTask(info,file,request);
    }

    @ApiOperation("任务分页")
    @GetMapping("/listTask")
    public Response listTask(@ApiParam(value = "分页每页展示数量", required = true) @RequestParam("pageSize") Integer pageSize,
                                 @ApiParam(value = "分页页码", required = true) @RequestParam("pageNo") Integer pageNo) {
        return Response.ok(taskService.listTask(pageSize, pageNo));
    }


    @ApiOperation("导出excel")
    @GetMapping("/downloadExcel")
    @CrossOrigin
    public void downloadExcel(HttpServletResponse response, String taskId){
        taskService.downloadExcel(response,taskId);

    }

    @ApiOperation("删除任务")
    @GetMapping("/deleteTask/{taskId}")
    public Response deleteArticle(@PathVariable("taskId")String taskId) throws Exception {
        return taskService.deleteTask(taskId);
    }

}
