package com.christer.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.exception.BusinessException;
import com.christer.project.model.dto.picture.PictureQueryParam;
import com.christer.project.model.dto.post.PostQueryParam;
import com.christer.project.model.dto.search.SearchQueryParam;
import com.christer.project.model.dto.user.UserQueryParam;
import com.christer.project.model.entity.picture.PictureEntity;
import com.christer.project.model.vo.PostVO;
import com.christer.project.model.vo.SearchVO;
import com.christer.project.model.vo.UserInfoVO;
import com.christer.project.service.PictureService;
import com.christer.project.service.PostService;
import com.christer.project.service.SearchService;
import com.christer.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-10 17:10
 * Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final UserService userService;

    private final PostService postService;

    private final PictureService pictureService;
    @Override
    public SearchVO searchAll(SearchQueryParam searchQueryParam) {
        final SearchVO searchVO = new SearchVO();
        CompletableFuture<Page<UserInfoVO>> userTask = CompletableFuture.supplyAsync(() -> {
            final UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setUserName(searchQueryParam.getSearchText());
            userQueryParam.setCurrentPage(searchQueryParam.getCurrentPage());
            userQueryParam.setPageSize(searchQueryParam.getPageSize());
            return userService.queryUserByCondition(userQueryParam);
        });
        // 查询用户
        CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
            // 查询帖子
            final PostQueryParam postQueryParam = new PostQueryParam();
            BeanUtil.copyProperties(searchQueryParam, postQueryParam);
            return postService.queryPostPage(postQueryParam);
        });

        CompletableFuture<Page<PictureEntity>> pictureTask = CompletableFuture.supplyAsync(() -> {
            // 查询图片
            final PictureQueryParam pictureQueryParam = new PictureQueryParam();
            BeanUtil.copyProperties(searchQueryParam, pictureQueryParam);
            return pictureService.queryPicturePage(pictureQueryParam);
        });

        CompletableFuture.allOf(userTask, postTask, pictureTask).join();
        try {
            Page<UserInfoVO> userInfoVOPage = userTask.get();
            Page<PostVO> postVOPage = postTask.get();
            Page<PictureEntity> pictureEntityPage = pictureTask.get();
            // 赋值
            searchVO.setUserList(userInfoVOPage.getRecords());
            searchVO.setPostList(postVOPage.getRecords());
            searchVO.setPictureList(pictureEntityPage.getRecords());

        } catch (Exception e) {
            log.error("聚合搜索异常:{0}", e);
            throw new BusinessException("查询出现错误...");
        }
        return searchVO;
    }
}
