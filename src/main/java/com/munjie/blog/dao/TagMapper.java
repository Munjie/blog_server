package com.munjie.blog.dao;

import com.munjie.blog.pojo.TagDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 21:54
 * @Description:
 */
@Repository
public interface TagMapper {

    List<String> listTagByArticleId(@Param("articleId")String articleId);


    List<TagDO> listTags();


    List<TagDO> listEditTags();



    int insertForeachTags(List<TagDO> list);

    int addTag(TagDO tagDO);
}
