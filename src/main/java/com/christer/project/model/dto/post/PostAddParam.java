package com.christer.project.model.dto.post;

import com.christer.project.util.ValidateGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-03 16:05
 * Description:
 */
@Setter
@Getter
@ToString
public class PostAddParam implements Serializable {

    private static final long serialVersionUID = -8667959933148330727L;
    /**
     * 标题
     */
    @NotBlank(groups = ValidateGroup.Save.class)
    private String title;

    /**
     * 内容
     */
    @NotBlank(groups = ValidateGroup.Save.class)
    private String content;

    /**
     * 标签列表
     */
    @NotEmpty(groups = ValidateGroup.Save.class)
    private List<String> tags;
}
