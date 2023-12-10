package com.christer.project.service;

import com.christer.project.model.dto.search.SearchQueryParam;
import com.christer.project.model.vo.SearchVO;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-10 17:10
 * Description:
 */
public interface SearchService {
    SearchVO searchAll(SearchQueryParam searchQueryParam);
}
