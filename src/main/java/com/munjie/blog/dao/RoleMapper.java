package com.munjie.blog.dao;

import com.munjie.blog.pojo.ModuleDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    List<ModuleDTO> listModuleById(@Param("id") String id);

    List<String> listParent();

    List<ModuleDTO> listModuleByParent(@Param("parent") String parent);
}
