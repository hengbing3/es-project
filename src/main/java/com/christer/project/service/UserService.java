package com.christer.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.model.dto.user.UserLoginParam;
import com.christer.project.model.dto.user.UserQueryParam;
import com.christer.project.model.dto.user.UserRegisterParam;
import com.christer.project.model.vo.UserInfoVO;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-02 15:07
 * Description:
 */
public interface UserService {
    /**
     * 用户注册
     * @param userParam 账号， 密码
     * @return id
     */
    Long registerUser(UserRegisterParam userParam);

    /**
     * 用户登录
     * @param userParam 账号，密码
     * @return token
     */
    UserInfoVO loginUser(UserLoginParam userParam);

    /**
     * 用户分页查询
     * @param userParam
     * @return
     */
    Page<UserInfoVO> queryUserByCondition(UserQueryParam userParam);
}
