package com.christer.project.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.christer.project.constant.CommonConstant;
import com.christer.project.mapper.PostMapper;
import com.christer.project.model.dto.post.PostAddParam;
import com.christer.project.model.dto.post.PostEsDTO;
import com.christer.project.model.dto.post.PostQueryParam;
import com.christer.project.model.entity.post.PostEntity;
import com.christer.project.model.vo.PostVO;
import com.christer.project.service.PostService;
import com.christer.project.util.BeanCopyUtil;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-03 15:49
 * Description:
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, PostEntity> implements PostService {

    private final PostMapper postMapper;

    private final SessionServiceImpl sessionService;

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);


    @Override
    public void addPost(PostAddParam postParam) {
        final PostEntity postEntity = new PostEntity();
        BeanUtils.copyProperties(postParam, postEntity);
        List<String> tags = postParam.getTags();
        String json = JSONUtil.toJsonStr(tags);
        postEntity.setUserId(sessionService.getCurrentUserInfo().getId());
        postEntity.setTags(json);
        postMapper.insert(postEntity);
    }

    @Override
    public Page<PostVO> queryPostPage(PostQueryParam postParam) {
        //分页参数
        final Page<PostEntity> rowPage = new Page<>(postParam.getCurrentPage(), postParam.getPageSize());
        final LambdaQueryWrapper<PostEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(postParam.getSearchText())) {
            queryWrapper.like(PostEntity::getContent, postParam.getSearchText())
                    .or()
                    .like(PostEntity::getTitle, postParam.getSearchText());
        }
        Page<PostEntity> postEntityPage = postMapper.selectPage(rowPage, queryWrapper);
        List<PostEntity> records = postEntityPage.getRecords();
        // 转vo
        List<PostVO> postVOS = BeanCopyUtil.copyListProperties(records, PostVO::new, (source, target) -> {
            String tags = source.getTags();
            JSONArray objects = JSONUtil.parseArray(tags);
            List<String> tagList = JSONUtil.toList(objects, String.class);
            target.setTagList(tagList);
        });
        // 转 page
        Page<PostVO> postVOPage = new Page<>(postParam.getCurrentPage(), postParam.getPageSize());
        postVOPage.setRecords(postVOS);
        postVOPage.setTotal(postVOS.size());
        return postVOPage;
    }

    @Override
    public Page<PostEntity> searchPostFromES(PostQueryParam postParam) {
        // 获取查询条件
        final Long id = postParam.getId();
        final Long notId = postParam.getNotId();
        final String searchText = postParam.getSearchText();
        final String title = postParam.getTitle();
        final String content = postParam.getContent();
        final List<String> tagList = postParam.getTags();
        final List<String> orTagList = postParam.getOrTags();
        final Long userId = postParam.getUserId();
        // 获取分页查询条件 es 当前页从 0 开始
        final int currentPage = postParam.getCurrentPage() - 1;
        final Integer pageSize = postParam.getPageSize();
        // 排序条件
        String sortField = postParam.getSortField();
        String sortOrder = postParam.getSortOrder();
        // 根据查询条件，拼接查询条件，调用es查询数据
        final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("deletedFlag", 0));
        if (id != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }
        if (notId != null) {
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", notId));
        }
        if (userId != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
        }
        // 必须包含所有标签
        if (!CollectionUtils.isEmpty(tagList)) {
            for (String tag : tagList) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("tags", tag));
            }
        }
        // 包含任意标签
        if (!CollectionUtils.isEmpty(orTagList)) {
            final BoolQueryBuilder orTagBoolQueryBuilder = QueryBuilders.boolQuery();
            for (String tag : orTagList) {
                orTagBoolQueryBuilder.should(QueryBuilders.termQuery("tags", tag));
            }
            orTagBoolQueryBuilder.minimumShouldMatch(1);
            boolQueryBuilder.filter(orTagBoolQueryBuilder);
        }
        // 按关键词检索
        if (StringUtils.hasText(searchText)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按标题检索
        if (StringUtils.hasText(title)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按内容检索
        if (StringUtils.hasText(content)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", content));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 排序
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        if (StringUtils.hasText(sortField)) {
            sortBuilder = SortBuilders.fieldSort(sortField);
            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
        }
        final PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
        // 构造查询
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .withSorts(sortBuilder)
                .build();
        final SearchHits<PostEsDTO> search = elasticsearchRestTemplate.search(searchQuery, PostEsDTO.class);
        // 将返回的数据转为 Java 对象 并从数据库中查询点赞数和收藏数
        Page<PostEntity> page = new Page<>();
        page.setTotal(search.getTotalHits());
        List<PostEntity> resourceList = Lists.newArrayList();
        if (search.hasSearchHits()) {
            final List<SearchHit<PostEsDTO>> searchHits = search.getSearchHits();
            // 获取es 查询的id
            List<Long> postIds = searchHits.stream().map(searchHit -> searchHit.getContent().getId())
                    .collect(Collectors.toList());
            List<PostEntity> postEntities = postMapper.selectBatchIds(postIds);
            if (!CollectionUtils.isEmpty(postEntities)) {
                final Map<Long, List<PostEntity>> postMap = postEntities.stream().collect(Collectors.groupingBy(PostEntity::getId));
                postIds.forEach(postId -> {
                    if (postMap.containsKey(postId)) {
                        resourceList.add(postMap.get(postId).get(0));
                    } else {
                        // 从 es 清空 db 已物理删除的数据
                        String delete = elasticsearchRestTemplate.delete(String.valueOf(postId), PostEsDTO.class);
                        log.info("delete post {}", delete);
                    }
                });
            }
        }
        page.setRecords(resourceList);
        // 返回page
        return page;
    }


}
