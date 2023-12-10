package com.christer.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.model.dto.picture.PictureQueryParam;
import com.christer.project.model.dto.post.PostQueryParam;
import com.christer.project.model.dto.search.SearchQueryParam;
import com.christer.project.model.dto.user.UserQueryParam;
import com.christer.project.model.entity.picture.PictureEntity;
import com.christer.project.model.entity.post.PostEntity;
import com.christer.project.model.vo.PostVO;
import com.christer.project.model.vo.SearchVO;
import com.christer.project.model.vo.UserInfoVO;
import com.christer.project.service.PictureService;
import com.christer.project.service.PostService;
import com.christer.project.service.SearchService;
import com.christer.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-10 17:10
 * Description:
 */
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final UserService userService;

    private final PostService postService;

    private final PictureService pictureService;
    @Override
    public SearchVO searchAll(SearchQueryParam searchQueryParam) {
        final SearchVO searchVO = new SearchVO();
        // 查询用户
       final UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserName(searchQueryParam.getSearchText());
        userQueryParam.setCurrentPage(searchQueryParam.getCurrentPage());
        userQueryParam.setPageSize(searchQueryParam.getPageSize());
        Page<UserInfoVO> userInfoVOPage = userService.queryUserByCondition(userQueryParam);
        // 查询帖子
       final PostQueryParam postQueryParam = new PostQueryParam();
        BeanUtil.copyProperties(searchQueryParam, postQueryParam);
        Page<PostVO> postEntityPage = postService.queryPostPage(postQueryParam);
        // 查询图片
        final PictureQueryParam pictureQueryParam = new PictureQueryParam();
        BeanUtil.copyProperties(searchQueryParam, pictureQueryParam);
        Page<PictureEntity> pictureEntityPage = pictureService.queryPicturePage(pictureQueryParam);
        // 赋值
        searchVO.setUserList(userInfoVOPage.getRecords());
        searchVO.setPostList(postEntityPage.getRecords());
        searchVO.setPictureList(pictureEntityPage.getRecords());
        return searchVO;
    }
}
