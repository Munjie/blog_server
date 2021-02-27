package com.munjie.blog.service;

import com.munjie.blog.pojo.TagDO;

import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:32
 * @Description:
 */
public interface TagService {

    List<TagDO> listEditTags();

    List<TagDO> listAllTags();

    int addTag(String tag);
}
