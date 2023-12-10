package com.christer.project.model.entity.picture;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-06 21:57
 * Description:
 * 图片实体
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class PictureEntity implements Serializable {

    private static final long serialVersionUID = -544240535183946836L;

    private String title;

    private String url;
}
