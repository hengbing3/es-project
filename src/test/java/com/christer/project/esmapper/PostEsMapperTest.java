package com.christer.project.esmapper;

import com.christer.project.model.dto.post.PostEsDTO;
import com.christer.project.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * @author Christer
 * @version 1.0
 * @date 2024-01-02 21:40
 * Description:
 * 帖子测试
 */
@SpringBootTest
@Slf4j
class PostEsMapperTest {

    @Resource
    private PostEsMapper postEsMapper;

    @Resource
    private PostService postService;

    @Test
    void findAll() {
        Page<PostEsDTO> postEsDTOS = postEsMapper.findAll(
                PageRequest.of(0, 5, Sort.by("createTime")));
        List<PostEsDTO> content = postEsDTOS.getContent();
        Assertions.assertNotNull(content);
        log.info("从ES获取帖子列表：{}", content);
    }

    /**
     * es 新增测试
     */
    @Test
    void testAdd() {
        PostEsDTO postEsDTO = new PostEsDTO();
        postEsDTO.setId(2L);
        postEsDTO.setTitle("我是张三，快快乐乐写代码！");
        postEsDTO.setContent("2024年，张三的代码水平提高，英语读写能力加强，非常棒！！！ 我的github 地址是 https://github.com/hengbing3");
        postEsDTO.setTags(Arrays.asList("java", "golang"));
        postEsDTO.setUserId(1L);
        postEsDTO.setCreateTime(new Date());
        postEsDTO.setUpdateTime(new Date());
        postEsDTO.setDeletedFlag(0);
        postEsMapper.save(postEsDTO);
        Assertions.assertEquals(2L, postEsDTO.getId());

    }

    @Test
    void testFindById() {
        Optional<PostEsDTO> postEsDTO = postEsMapper.findById(1L);
        Assertions.assertNotNull(postEsDTO);
        log.info("根据id获取数据：{}", postEsDTO);
    }

    @Test
    void testCount() {
        long count = postEsMapper.count();
        Assertions.assertEquals(1, count);
        log.info("获取数量：{}", count);
    }

    @Test
    void testFindByUserId() {
        List<PostEsDTO> postEsDaoTestList = postEsMapper.findByUserId(1L);
        Assertions.assertNotNull(postEsDaoTestList);
        log.info("根据用户id,获取列表:{}", postEsDaoTestList);
    }

    @Test
    void testFindByTitle() {
        List<PostEsDTO> byTitle = postEsMapper.findByTitle("我是阿槟");
        Assertions.assertNotNull(byTitle);
        log.info("根据标题匹配结果：{}", byTitle);
    }

    @Test
    void testDelete() {
        postEsMapper.deleteById(1L);
    }
}
