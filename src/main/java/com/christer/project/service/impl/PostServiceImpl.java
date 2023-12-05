package com.christer.project.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.christer.project.mapper.PostMapper;
import com.christer.project.model.dto.post.PostAddParam;
import com.christer.project.model.dto.post.PostQueryParam;
import com.christer.project.model.entity.post.PostEntity;
import com.christer.project.model.entity.user.UserEntity;
import com.christer.project.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
    public Page<PostEntity> queryPostPage(PostQueryParam postParam) {
        //分页参数
        final Page<PostEntity> rowPage = new Page<>(postParam.getCurrentPage(), postParam.getPageSize());
        final LambdaQueryWrapper<PostEntity> queryWrapper = new LambdaQueryWrapper<>();
        Page<PostEntity> page = postMapper.selectPage(rowPage, queryWrapper);
        return page;
    }
}
