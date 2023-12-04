package com.christer.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.model.dto.post.PostAddParam;
import com.christer.project.model.dto.post.PostQueryParam;
import com.christer.project.model.entity.post.PostEntity;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-03 15:48
 * Description:
 */
public interface PostService {
    void addPost(PostAddParam postParam);

    Page<PostEntity> queryPostPage(PostQueryParam postParam);
}
