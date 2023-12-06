package com.christer.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.WebURLConstant;
import com.christer.project.common.CommonResult;
import com.christer.project.common.ResultBody;
import com.christer.project.model.dto.picture.PictureQueryParam;
import com.christer.project.model.entity.picture.PictureEntity;
import com.christer.project.service.PictureService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-02 16:51
 * Description:
 */
@RestController
@RequiredArgsConstructor
//@SaCheckLogin
public class PictureController {

    private static final Logger log = LoggerFactory.getLogger(PictureController.class);

    private final PictureService pictureService;



    @PostMapping(WebURLConstant.URI_PICTURE_PAGE)
    @ApiOperation("分页获取图片")
    public CommonResult<Page<PictureEntity>> queryPostPage(@RequestBody @Validated PictureQueryParam pictureQueryParam) {
        log.info("query picture page param: {}", pictureQueryParam);
        final Page<PictureEntity> pictures =  pictureService.queryPicturePage(pictureQueryParam);
        return ResultBody.success(pictures);
    }


}
