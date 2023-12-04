package com.christer.project.model.entity.post;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-02 16:57
 * Description:
 */
@Setter
@Getter
@ToString
@TableName(value = "post")
public class PostEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 3233627880861641668L;
    /**
     * 帖子ID，主键，自增长
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（JSON 数组）
     */
    private String tags;

    /**
     * 点赞数，默认为0
     */
    private Integer thumbNum;

    /**
     * 收藏数，默认为0
     */
    private Integer favourNum;

    /**
     * 创建用户ID
     */
    private Long userId;

    /**
     * 创建时间，默认为当前时间
     */
    private Date createTime;

    /**
     * 更新时间，默认为当前时间，自动更新
     */
    private Date updateTime;

    /**
     * 是否删除的标志，默认为0（未删除）
     */
    private Integer deletedFlag;
}
