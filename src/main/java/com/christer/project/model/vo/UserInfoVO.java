package com.christer.project.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-02 17:16
 * Description:
 */
@Setter
@Getter
@ToString
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = -9153649974240243671L;

    private Long id;
    /**
     * 账号
     */
    private String userAccount;
    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private String token;
}
