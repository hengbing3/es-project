package com.christer.project.model.dto.picture;

import com.christer.project.common.PageCondition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-06 22:44
 * Description:
 * 图片查询条件
 */
@Setter
@Getter
@ToString(callSuper = true)
public class PictureQueryParam extends PageCondition implements Serializable {

    private static final long serialVersionUID = -4231156941386945329L;
    /**
     * 搜索词
     */
//    @NotBlank(message = "搜索词不能为空!")
    private String searchText;
}
