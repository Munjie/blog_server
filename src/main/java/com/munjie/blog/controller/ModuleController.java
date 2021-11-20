package com.munjie.blog.controller;


import com.munjie.blog.pojo.ModuleDO;
import com.munjie.blog.pojo.Response;
import com.munjie.blog.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 86158
 */
@Validated
@Api(tags = "模块管理API")
@RestController
@RequestMapping("/module")
@CrossOrigin
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @PostMapping("/addModule")
    @ApiOperation("处理excel")
    public Response addModule(@RequestPart ModuleDO moduleDO) {
        return moduleService.addModule(moduleDO);
    }

    @ApiOperation("删除任务")
    @GetMapping("/deleteModule/{id}")
    public Response deleteArticle(@PathVariable("id")Integer id) throws Exception {
        return moduleService.deleteModule(id);
    }


    @ApiOperation("导出excel")
    @GetMapping("/updateModule")
    @CrossOrigin
    public Response updateModule(@RequestPart ModuleDO moduleDO){
        return moduleService.updateModule(moduleDO);

    }

    @ApiOperation("删除任务")
    @GetMapping("/listModule")
    public Response listModule(@ApiParam(value = "分页每页展示数量", required = true) @RequestParam("pageSize") Integer pageSize,
                               @ApiParam(value = "分页页码", required = true) @RequestParam("pageNo") Integer pageNo) throws Exception {
        return Response.ok(moduleService.listModule(pageSize,pageNo));
    }




}
