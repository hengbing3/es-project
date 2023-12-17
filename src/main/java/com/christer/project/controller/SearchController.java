package com.christer.project.controller;

import com.christer.project.WebURLConstant;
import com.christer.project.common.CommonResult;
import com.christer.project.common.ResultBody;
import com.christer.project.manager.SearchFacade;
import com.christer.project.model.dto.search.SearchQueryParam;
import com.christer.project.model.vo.SearchVO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-10 16:51
 * Description:
 * 聚合搜索Controller
 */
@RestController
@RequiredArgsConstructor
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);


    private final SearchFacade searchFacade;

    @PostMapping(WebURLConstant.URI_SEARCH_PAGE)
    @ApiOperation("聚合搜索-分页查询")
    public CommonResult<SearchVO> searchAll(@RequestBody SearchQueryParam searchQueryParam) {
        log.info("search params: {}", searchQueryParam);
        final SearchVO searchVO = searchFacade.searchAll(searchQueryParam);
        return ResultBody.success(searchVO);
    }
}
