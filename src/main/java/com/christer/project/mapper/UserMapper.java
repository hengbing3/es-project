package com.christer.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.christer.project.model.entity.user.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-02 15:49
 * Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
