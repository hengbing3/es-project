package com.christer.project.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-17 17:16
 * Description:
 * 数据源接口-新接入的数据源必须实现
 */
public interface DataSource<T> {
    /**
     * 搜索公共接口
     * @param searchText 搜索关键词
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return <T> 返回类型页面
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
