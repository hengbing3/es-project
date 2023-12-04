package com.christer.project.common;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-02 15:15
 * Description:
 * 公共实体抽象属性
 */
@Setter
@Getter
@ToString
public abstract class AbstractObjectModel {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private Boolean deletedFlag;
}
