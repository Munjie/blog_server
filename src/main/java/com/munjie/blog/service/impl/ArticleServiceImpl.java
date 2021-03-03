package com.munjie.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.munjie.blog.config.MyResultMapper;
import com.munjie.blog.dao.ArticleMapper;
import com.munjie.blog.dao.ArticleRepository;
import com.munjie.blog.dao.TagMapper;
import com.munjie.blog.pojo.ArticleDO;
import com.munjie.blog.pojo.ArticleDTO;
import com.munjie.blog.pojo.TagDO;
import com.munjie.blog.service.ArticleService;
import com.munjie.blog.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Auther: munjie
 * @Date: 2/19/2021 22:15
 * @Description:
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Autowired
    private MyResultMapper myResultMapper;


    @Autowired
    private ArticleMapper articleMapper;


    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TransportClient client;

    @Override
    public ArticleDTO listArticles(Integer pageSize, Integer pageNo) {
        LOGGER.info("");
        PageHelper.startPage(pageNo, pageSize);
        List<ArticleDO> list = articleMapper.listArticle();
        list.forEach(article ->
                article.setTags(tagMapper.listTagByArticleId(article.getArticleId()))
        );
        PageInfo<ArticleDO> articleDOPageInfo = new PageInfo<>(list);
        return (new ArticleDTO(articleDOPageInfo.getList(),articleDOPageInfo.getTotal()));
    }

    @Override
    public int saveArticle(ArticleDO articleDO) {
        articleDO.setArticleCreateTime(new Date());
        articleDO.setArticleId(DateUtil.getBlogId());
        articleDO.setStatus("否");
        int i = articleMapper.saveArticle(articleDO);
        return i;
    }

    @Override
    public ArticleDO getArticleDetailById(String articleId) {
        int i = articleMapper.updateArticleView(articleId);
        ArticleDO articleDetailById = articleMapper.getArticleDetailById(articleId);
        List<String> tagDOS = tagMapper.listTagByArticleId(articleId);
        articleDetailById.setTags(tagDOS);
        return articleDetailById;
    }

    @Override
    public int deleteArticle(String articleId) {
        articleRepository.deleteById(articleId);
            articleRepository.deleteById(articleId);
            DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
                    .newRequestBuilder(client)
                    .filter(QueryBuilders.termQuery("articleId", articleId)).source("blog");
            BulkByScrollResponse response = builder.get();
            long deleted = response.getDeleted();
            LOGGER.info("删除的条数为：{}",deleted);
        return articleMapper.deleteArticle(articleId);
    }


    @Override
    public ArticleDTO listSearch(Integer pageSize, Integer pageNo, String content) {

        Sort createTime = Sort.by("articleCreateTime").descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize,createTime);
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("articleContent", content);
        QueryBuilders.commonTermsQuery("articleContent",content);
        List<ArticleDO> list = new ArrayList<>();
        HighlightBuilder.Field allHighLight = new HighlightBuilder.Field("articleContent").
                preTags("<span style=\"color:red\">").postTags("</span>").requireFieldMatch(false);
        HighlightBuilder.Field[] ary = new HighlightBuilder.Field[1];
        ary[0] = allHighLight;
        SearchQuery query = new NativeSearchQueryBuilder().
                withQuery(QueryBuilders.matchQuery("articleContent",content)).  //需要查询的数据以及对应的字段
                withHighlightFields(ary).
                withPageable(pageable).
                build();
        //搜索
        Page<ArticleDO> search = elasticsearchTemplate.queryForPage(query, ArticleDO.class,myResultMapper);
        long totalElements = search.getTotalElements();
        Iterator<ArticleDO> iterator = search.iterator();
        while (iterator.hasNext()){
            ArticleDO next = iterator.next();
            list.add(next);
        }
        PageInfo<ArticleDO> articleDOPageInfo = new PageInfo<>(list);
        return (new ArticleDTO(articleDOPageInfo.getList(),totalElements));
    }

    @Override
    public int publishArticle(ArticleDO articleDO) {
        int i;
        if (StringUtils.isNotEmpty(articleDO.getArticleId())){
            articleDO.setArticleCreateTime(new Date());
            articleDO.setArticleUpdateTime(new Date());
            articleDO.setArticlePublishTime(new Date());
            i = articleMapper.updateArticleById(articleDO);
        }else{
            articleDO.setArticleCreateTime(new Date());
            articleDO.setArticlePublishTime(new Date());
            articleDO.setArticleViews(1);
            // articleDO.setStatus("");
            String blogId = DateUtil.getBlogId();
            articleDO.setArticleId(blogId);
            List<String> tags = articleDO.getTags();
            List<TagDO> tagDOList= new ArrayList<>();
            for (String str: tags) {
                if (StringUtils.isNotEmpty(str)) {
                    TagDO tagDO = new TagDO();
                    tagDO.setTagName(str);
                    tagDO.setArticleId(blogId);
                    tagDOList.add(tagDO);
                }
            }
            i = tagMapper.insertForeachTags(tagDOList);
            i = articleMapper.saveArticle(articleDO);
        }
        ArticleDO save = articleRepository.save(articleDO);
        return i;
    }

    @Override
    public ArticleDTO listBack(Integer pageSize, Integer pageNo) {
        PageHelper.startPage(pageNo, pageSize);
        List<ArticleDO> list = articleMapper.listBack();
        PageInfo<ArticleDO> articleDOPageInfo = new PageInfo<>(list);
        return (new ArticleDTO(articleDOPageInfo.getList(),articleDOPageInfo.getTotal()));
    }

    @Override
    public ArticleDTO tagArticles(Integer pageSize, Integer pageNo, String tagName) {
        PageHelper.startPage(pageNo, pageSize);
        List<ArticleDO> list = articleMapper.listArticleByTag(tagName);
        list.forEach(article ->
                article.setTags(tagMapper.listTagByArticleId(article.getArticleId()))
        );
        PageInfo<ArticleDO> articleDOPageInfo = new PageInfo<>(list);
        return (new ArticleDTO(articleDOPageInfo.getList(),articleDOPageInfo.getTotal()));
    }

    @Override
    public int updateStatusById(String articleId, String status) {
        return articleMapper.updateStatusById(articleId,status);
    }
}
