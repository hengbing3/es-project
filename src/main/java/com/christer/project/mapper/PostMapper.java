package com.christer.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.christer.project.model.entity.post.PostEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-02 16:52
 * Description:
 */
@Mapper
public interface PostMapper extends BaseMapper<PostEntity> {
}
