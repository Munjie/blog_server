package com.munjie.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.munjie.blog.dao.ModuleMapper;
import com.munjie.blog.pojo.*;
import com.munjie.blog.service.ModuleService;
import com.munjie.blog.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 86158
 */
@Transactional
@Service
public class ModuleServiceImpl implements ModuleService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ModuleServiceImpl.class);

    @Autowired
    private ModuleMapper moduleMapper;

    @Override
    public Response addModule(ModuleDO moduleDO) {
        moduleDO.setCreateTime(new Date());
        moduleDO.setModuleId("MD"+System.currentTimeMillis());
        moduleMapper.addModule(moduleDO);
        return Response.ok("新增成功");
    }

    @Override
    public Response deleteModule(int id) {
        moduleMapper.deleteModule(id);
        return Response.ok("删除成功");
    }

    @Override
    public Response updateModule(ModuleDO moduleDO) {
        moduleDO.setUpdateTime(new Date());
        moduleMapper.updateModule(moduleDO);
        return Response.ok("更新成功");
    }

    @Override
    public ModulesDTO listModule(Integer pageSize, Integer pageNo) {
        PageHelper.startPage(pageNo, pageSize);
        List<ModuleDO> list = moduleMapper.listModule();
        for (ModuleDO moduleDO:list) {
            if (moduleDO != null) {
                moduleDO.setCreateDate(CommonUtil.formatDate(moduleDO.getCreateTime()));
            }
        }
        PageInfo<ModuleDO> moduleDOPageInfo = new PageInfo<>(list);
        return (new ModulesDTO(moduleDOPageInfo.getList(),moduleDOPageInfo.getTotal()));
    }
}
