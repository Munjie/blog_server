package com.munjie.blog.service.impl;

import com.munjie.blog.dao.TagMapper;
import com.munjie.blog.pojo.TagDO;
import com.munjie.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:19
 * @Description:
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Override
    public List<TagDO> listEditTags() {
        return tagMapper.listEditTags();
    }

    @Override
    public List<TagDO> listAllTags() {
        return tagMapper.listTags();
    }

    @Override
    public int addTag(String tag) {
        TagDO tagDO = new TagDO();
        tagDO.setArticleId(null);
        tagDO.setTagName(tag);
        return tagMapper.addTag(tagDO);
    }
}