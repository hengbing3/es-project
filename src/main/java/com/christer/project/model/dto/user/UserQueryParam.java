package com.christer.project.model.dto.user;

import com.christer.project.common.PageCondition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-03 15:28
 * Description:
 * 用户列表查询请求参数
 */
@Setter
@Getter
@ToString(callSuper = true)
public class UserQueryParam extends PageCondition implements Serializable {


    private static final long serialVersionUID = 1839612359575139632L;
    /**
     * id
     */
    private Long id;

    /**
     * 开放平台id
     */
    private String unionId;

    /**
     * 公众号openId
     */
    private String mpOpenId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;
}
