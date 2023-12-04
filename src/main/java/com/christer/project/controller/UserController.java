package com.christer.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.WebURLConstant;
import com.christer.project.common.CommonResult;
import com.christer.project.common.ResultBody;
import com.christer.project.model.dto.user.UserLoginParam;
import com.christer.project.model.dto.user.UserQueryParam;
import com.christer.project.model.dto.user.UserRegisterParam;
import com.christer.project.model.vo.UserInfoVO;
import com.christer.project.service.UserService;
import com.christer.project.service.impl.SessionServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-10-08 17:36
 * Description:
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final SessionServiceImpl sessionService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @PostMapping(WebURLConstant.URI_USER_REGISTER)
    @ApiOperation("用户注册")
    public CommonResult<Long> registerUser(@RequestBody @Validated UserRegisterParam userParam) {
        log.info("register user param: {}", userParam);
        final Long id = userService.registerUser(userParam);
        return ResultBody.success(id);
    }

    @PostMapping(WebURLConstant.URI_USER_LOGIN)
    @ApiOperation("用户登录")
    public CommonResult<String> loginUser(@RequestBody @Validated UserLoginParam userParam) {
        log.info("login user param: {}", userParam);
        final UserInfoVO userInfoVO = userService.loginUser(userParam);
        sessionService.login(userInfoVO);
        return ResultBody.success(sessionService.getCurrentUserInfo().getToken());
    }

    @PostMapping(WebURLConstant.URI_USER_PAGE)
    @ApiOperation("用户分页查询")
    public CommonResult<Page<UserInfoVO>> queryUserByCondition(@RequestBody @Validated UserQueryParam userParam) {
        log.info("user page condition:{}" , userParam);
        final Page<UserInfoVO> userInfoVOPage = userService.queryUserByCondition(userParam);
        return ResultBody.success(userInfoVOPage);
    }



}
