package com.christer.project.datasource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.mapper.UserMapper;
import com.christer.project.model.entity.user.UserEntity;
import com.christer.project.model.vo.UserInfoVO;
import com.christer.project.util.BeanCopyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-02 15:48
 * Description:
 */
@Service
@RequiredArgsConstructor
public class UserDataSource implements DataSource<UserInfoVO> {

    private final UserMapper userMapper;


    @Override
    public Page<UserInfoVO> doSearch(String searchText, long pageNum, long pageSize) {
        //分页参数
        final Page<UserEntity> rowPage = new Page<>(pageNum, pageSize);
        final LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        // 添加用户查询条件
        queryWrapper.like(org.springframework.util.StringUtils.hasLength(searchText), UserEntity::getUserName, searchText);
        Page<UserEntity> userEntityPage = userMapper.selectPage(rowPage, queryWrapper);
        List<UserInfoVO> userInfoVOS = BeanCopyUtil.copyListProperties(userEntityPage.getRecords(), UserInfoVO::new);
        Page<UserInfoVO> userInfoPage = new Page<>(userEntityPage.getCurrent(), userEntityPage.getSize(), userEntityPage.getTotal());
        userInfoPage.setRecords(userInfoVOS);
        return userInfoPage;
    }
}
