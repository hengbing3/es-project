package com.christer.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.christer.project.model.dto.post.PostAddParam;
import com.christer.project.model.dto.post.PostQueryParam;
import com.christer.project.model.entity.post.PostEntity;
import com.christer.project.model.vo.PostVO;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-03 15:48
 * Description:
 */
public interface PostService extends IService<PostEntity> {
    void addPost(PostAddParam postParam);

    Page<PostVO> queryPostPage(PostQueryParam postParam);

    /**
     * 通过ES 查询帖子信息
     * @param postParam id，searchText...
     * @return page
     */
    Page<PostEntity> searchPostFromES(PostQueryParam postParam);
}
