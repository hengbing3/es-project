package com.christer.project.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.christer.project.mapper.PostMapper;
import com.christer.project.model.dto.post.PostAddParam;
import com.christer.project.model.dto.post.PostQueryParam;
import com.christer.project.model.entity.post.PostEntity;
import com.christer.project.model.vo.PostVO;
import com.christer.project.service.PostService;
import com.christer.project.util.BeanCopyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;



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
}
