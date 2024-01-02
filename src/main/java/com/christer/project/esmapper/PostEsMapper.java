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

    List<PostEsDTO> findByUserId(Long userId);

    List<PostEsDTO> findByTitle(String title);
}
