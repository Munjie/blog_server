package com.munjie.blog.dao;

import com.munjie.blog.pojo.ModuleDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 86158
 */
@Repository
public interface ModuleMapper {


    int addModule(ModuleDO moduleDO);

    int deleteModule(int id);

    int updateModule(ModuleDO moduleDO);

    List<ModuleDO> listModule();

}