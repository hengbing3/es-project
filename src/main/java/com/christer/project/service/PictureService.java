package com.christer.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.model.dto.picture.PictureQueryParam;
import com.christer.project.model.entity.picture.PictureEntity;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-06 22:46
 * Description:
 */
public interface PictureService {
    /**
     * 分页获取图片
     * @param pictureQueryParam 图片请求参数，搜索词...
     * @return page
     */
    Page<PictureEntity> queryPicturePage(PictureQueryParam pictureQueryParam);
}
