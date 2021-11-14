package com.munjie.blog.controller;


import com.munjie.blog.dao.AddressMapper;
import com.munjie.blog.pojo.AddressDO;
import com.munjie.blog.pojo.Response;
import com.munjie.blog.pojo.TaskInfoDO;
import com.munjie.blog.service.TaskService;
import com.munjie.blog.utils.ExportExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;

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
    @PostMapping("/exportExcel")
    public void exportExcel(String taskId,HttpServletResponse response)throws Exception{
        taskService.exportExcel(taskId,response);
    }

    //@RequestMapping(value = "/downloadExcel",method = RequestMethod.GET)
    @ApiOperation("下载excel")
    @GetMapping("/downloadExcel")
    @CrossOrigin
    public void downloadExcel(HttpServletResponse response, String taskId){
        //得到所有要导出的数据
        List<AddressDO> addressDOList =addressMapper.listAddress(taskId);
        //定义导出的excel名字
        String excelName = "路程";

        //获取需要转出的excel表头的map字段
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        fieldMap.put("province","省份");
        fieldMap.put("city","地级市");
        fieldMap.put("country","县城（区县）");
        fieldMap.put("address","城市（具体地址）");
        fieldMap.put("distance","公里数");
        fieldMap.put("duration","送货时间 （天）");

        //导出用户相关信息
        ExportExcelUtils.export(excelName,addressDOList,fieldMap,response);
    }

}
