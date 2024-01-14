package com.christer.project.esmapper;

import com.christer.project.model.dto.post.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author Christer
 * @version 1.0
 * @date 2024-01-02 21:27
 * Description:
 * 帖子 ES 操作Mapper
 */
public interface PostEsMapper extends ElasticsearchRepository<PostEsDTO, Long> {

    /**
     * 自定义方法：根据用户id查询帖子信息
     * @param userId 用户id
     * @return 帖子详情列表
     */
    List<PostEsDTO> findByUserId(Long userId);

    /**
     * 自定义方法：根据标题查询帖子信息
     * @param title 标题
     * @return 帖子详情列表
     */
    List<PostEsDTO> findByTitle(String title);
}
