package com.christer.project.model.dto.post;

import com.christer.project.common.PageCondition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-03 17:06
 * Description:
 */
@Setter
@Getter
@ToString
public class PostQueryParam extends PageCondition implements Serializable {

    private static final long serialVersionUID = -7008210785088759028L;

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;
}
