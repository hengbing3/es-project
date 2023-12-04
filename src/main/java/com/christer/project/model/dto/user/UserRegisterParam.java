package com.christer.project.model.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-02 15:10
 * Description:
 * 用户注册请求参数
 */
@Setter
@Getter
@ToString
public class UserRegisterParam implements Serializable {

    private static final long serialVersionUID = 1358724242709996116L;
    /**
     * 账号
     */
    @Length(max = 16, message = "账号最大支持16位！")
    @NotBlank(message = "账户不能为空！")
    private String userAccount;
    /**
     * 密码
     */
    @Length(max = 16, message = "密码最大支持16位！")
    @NotBlank(message = "密码不能为空！")
    private String userPassword;
    /**
     * 确认密码
     */
    @Length(max = 16, message = "密码最大支持16位！")
    @NotBlank(message = "请确认密码！")
    private String checkPassword;
    /**
     * 微信开放平台di
     */
    private String unionId;
    /**
     * 微信公众号openId
     */
    private String mpOpenId;
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
     * 用户角色
     */
    private String userRole;
}
