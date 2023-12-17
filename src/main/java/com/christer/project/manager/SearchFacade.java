package com.christer.project.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.datasource.*;
import com.christer.project.exception.BusinessException;
import com.christer.project.exception.ThrowUtils;
import com.christer.project.model.dto.search.SearchQueryParam;
import com.christer.project.model.entity.picture.PictureEntity;
import com.christer.project.model.enums.SearchTypeEnum;
import com.christer.project.model.vo.PostVO;
import com.christer.project.model.vo.SearchVO;
import com.christer.project.model.vo.UserInfoVO;
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

    private final UserDataSource userDataSource;

    private final PostDataSource postDataSource;

    private final PictureDatasource pictureDataSource;

    private final DataSourceRegistry dataSourceRegistry;





    public SearchVO searchAll(SearchQueryParam searchQueryParam) {
        // 获取搜索类型
        final String searchType = searchQueryParam.getType();
        ThrowUtils.throwIf(!StringUtils.hasText(searchType), "搜索类型不存在!");
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(searchType);
        final String searchText = searchQueryParam.getSearchText();
        final Integer current = searchQueryParam.getCurrentPage();
        final Integer pageSize = searchQueryParam.getPageSize();
        // 搜索类型为空，搜索出所有类型
        final SearchVO searchVO = new SearchVO();
        if (null == searchTypeEnum) {
            // 查询用户
            CompletableFuture<Page<UserInfoVO>> userTask = CompletableFuture.supplyAsync(() -> userDataSource.doSearch(searchText, current, pageSize));
            // 查询帖子
            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> postDataSource.doSearch(searchText, current, pageSize));
            // 查询图片
            CompletableFuture<Page<PictureEntity>> pictureTask = CompletableFuture.supplyAsync(() -> pictureDataSource.doSearch(searchText, current,  pageSize));

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
            // 利用注册器模式提前存储好要调用的对象
            DataSource<?> tDataSource = dataSourceRegistry.getDataSourceByType(searchTypeEnum.getValue());
            Page<?> page = tDataSource.doSearch(searchText, current, pageSize);
            searchVO.setDataList(page.getRecords());
        }

        return searchVO;
    }
}
