package com.christer.project.manager;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.exception.BusinessException;
import com.christer.project.exception.ThrowUtils;
import com.christer.project.model.dto.picture.PictureQueryParam;
import com.christer.project.model.dto.post.PostQueryParam;
import com.christer.project.model.dto.search.SearchQueryParam;
import com.christer.project.model.dto.user.UserQueryParam;
import com.christer.project.model.entity.picture.PictureEntity;
import com.christer.project.model.enums.SearchTypeEnum;
import com.christer.project.model.vo.PostVO;
import com.christer.project.model.vo.SearchVO;
import com.christer.project.model.vo.UserInfoVO;
import com.christer.project.service.PictureService;
import com.christer.project.service.PostService;
import com.christer.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.CompletableFuture;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-17 16:57
 * Description:
 * 聚合搜索门面
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SearchFacade {

    private final UserService userService;

    private final PostService postService;

    private final PictureService pictureService;



    public SearchVO searchAll(SearchQueryParam searchQueryParam) {
        // 获取搜索类型
        final String searchType = searchQueryParam.getType();
        ThrowUtils.throwIf(!StringUtils.hasText(searchType), "搜索类型不存在!");
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(searchType);

        // 搜索类型为空，搜索出所有类型
        final SearchVO searchVO = new SearchVO();
        if (null == searchTypeEnum) {
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
        } else {
            switch (searchTypeEnum) {
                case USER:
                    // 查询用户
                    final UserQueryParam userQueryParam = new UserQueryParam();
                    userQueryParam.setUserName(searchQueryParam.getSearchText());
                    userQueryParam.setCurrentPage(searchQueryParam.getCurrentPage());
                    userQueryParam.setPageSize(searchQueryParam.getPageSize());
                    Page<UserInfoVO> userInfoVOPage = userService.queryUserByCondition(userQueryParam);
                    searchVO.setUserList(userInfoVOPage.getRecords());
                    break;
                case POST:
                    // 查询帖子
                    final PostQueryParam postQueryParam = new PostQueryParam();
                    BeanUtil.copyProperties(searchQueryParam, postQueryParam);
                    Page<PostVO> postVOPage = postService.queryPostPage(postQueryParam);
                    searchVO.setPostList(postVOPage.getRecords());
                    break;
                case PICTURE:
                    // 查询图片
                    final PictureQueryParam pictureQueryParam = new PictureQueryParam();
                    BeanUtil.copyProperties(searchQueryParam, pictureQueryParam);
                    Page<PictureEntity> pictureEntityPage = pictureService.queryPicturePage(pictureQueryParam);
                    searchVO.setPictureList(pictureEntityPage.getRecords());
                    break;
                default:
            }
        }

        return searchVO;
    }
}
