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
 * @date 2023-12-03 14:42
 * Description:
 * 用户登录请求参数
 */
@Setter
@Getter
@ToString
public class UserLoginParam implements Serializable {

    private static final long serialVersionUID = -7317600980873577751L;

    @Length(max = 16, message = "账号最大支持16位！")
    @NotBlank(message = "账户不能为空！")
    private String userAccount;

    @Length(max = 16, message = "密码最大支持16位！")
    @NotBlank(message = "密码不能为空！")
    private String userPassword;
}
