package com.munjie.blog.controller;


import com.munjie.blog.pojo.Response;
import com.munjie.blog.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Api(tags = "地址处理接口")
@RestController
@RequestMapping("/address")
@CrossOrigin
public class AddressConller {

    @Autowired
    private AddressService addressService;


    @ApiOperation("地址分页")
    @GetMapping("/listAddress")
    public Response listTask(@ApiParam(value = "分页每页展示数量", required = true) @RequestParam("pageSize") Integer pageSize,
                                 @ApiParam(value = "分页页码", required = true) @RequestParam("pageNo") Integer pageNo, @ApiParam(value = "任务id", required = true) @RequestParam("taskId") String taskId) {
        return Response.ok(addressService.listAddress(pageSize,pageNo,taskId));
    }



}
