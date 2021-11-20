package com.munjie.blog.service;

import com.munjie.blog.pojo.ModuleDO;
import com.munjie.blog.pojo.ModulesDTO;
import com.munjie.blog.pojo.Response;

/**
 * @author 86158
 */
public interface ModuleService {


    Response addModule(ModuleDO moduleDO);

    Response deleteModule(int id);

    Response updateModule(ModuleDO moduleDO);

    ModulesDTO listModule(Integer pageSize, Integer pageNo);

}