package com.christer.project.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.WebURLConstant;
import com.christer.project.common.CommonResult;
import com.christer.project.common.ResultBody;
import com.christer.project.model.dto.post.PostAddParam;
import com.christer.project.model.dto.post.PostQueryParam;
import com.christer.project.model.entity.post.PostEntity;
import com.christer.project.model.vo.PostVO;
import com.christer.project.service.PostService;
import com.christer.project.util.ValidateGroup;
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
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

    @PostMapping(WebURLConstant.URI_POST)
    @ApiOperation("新增帖子")
    public CommonResult<Void> addPost(@RequestBody @Validated({ValidateGroup.Save.class}) PostAddParam postParam) {
        log.info("add post param: {}", postParam);
        postService.addPost(postParam);
        return ResultBody.success();
    }

    @PostMapping(WebURLConstant.URI_POST_PAGE)
    @ApiOperation("帖子列表")
    public CommonResult<Page<PostEntity>> queryPostPage(@RequestBody @Validated PostQueryParam postParam) {
        log.info("query post page param: {}", postParam);
        final Page<PostEntity> postEntityPage =  postService.queryPostPage(postParam);
        return ResultBody.success(postEntityPage);
    }


}
