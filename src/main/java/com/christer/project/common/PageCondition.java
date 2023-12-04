package com.christer.project.common;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-03 15:02
 * Description:
 * 分页参数
 */
@Setter
@Getter
public class PageCondition {
    /**
     * 当前页数
     */
    @NotNull(message = "当前页数不能为空！")
    private Integer currentPage;
    /**
     * 页面大小
     */
    @NotNull(message = "页数大小不能为空！")
    private Integer pageSize;
    /**
     * 当前查询开始记录
     */
    private Integer start;

    public Integer getStart() {
        return (currentPage - 1) * pageSize;
    }
}
