package com.christer.project.model.dto.search;

import com.christer.project.common.PageCondition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-10 17:02
 * Description:
 * 聚合搜索 请求参数
 */
@Setter
@Getter
@ToString
public class SearchQueryParam extends PageCondition implements Serializable {

    private static final long serialVersionUID = -2874367933540911183L;

    /**
     * 搜索关键词
     */
    private String searchText;

}
