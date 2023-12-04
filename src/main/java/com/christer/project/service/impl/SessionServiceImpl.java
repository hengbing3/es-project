package com.christer.project.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.christer.project.model.dto.user.UserLoginParam;
import com.christer.project.model.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-02 17:21
 * Description:
 */
@Service
@Slf4j
public class SessionServiceImpl {

    private static final String USER_INFO_KEY = "UserLoginValue";

    public void login(UserInfoVO currentUser) {
        StpUtil.login(currentUser.getId());
        currentUser.setToken(StpUtil.getTokenValue());
        StpUtil.getSession().set(USER_INFO_KEY, currentUser);
    }

    public UserInfoVO getCurrentUserInfo() {
        StpUtil.checkLogin();
        final UserInfoVO userInfo = (UserInfoVO) StpUtil.getSession().get(USER_INFO_KEY);
        log.info("从Session中获取当前用户信息:{}", userInfo);
        return userInfo;
    }

    public void logout() {
        log.info("用户登出:{}", StpUtil.getTokenValue());
        StpUtil.logout();
    }


}
