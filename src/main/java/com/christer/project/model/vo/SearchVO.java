package com.christer.project.model.vo;

import com.christer.project.model.entity.picture.PictureEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-10 17:08
 * Description:
 */
@Setter
@Getter
@ToString
public class SearchVO implements Serializable {

    private static final long serialVersionUID = -2176710104665325312L;
    /**
     * 用户列表
     */
    private List<UserInfoVO> userList;
    /**
     * 帖子列表
     */
    private List<PostVO> postList;
    /**
     * 图片列表
     */
    private List<PictureEntity> pictureList;
}
