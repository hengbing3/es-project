package com.christer.project;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.christer.project.mapper.PostMapper;
import com.christer.project.model.entity.picture.PictureEntity;
import com.christer.project.model.entity.post.PostEntity;
import com.christer.project.service.PostService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-04 22:04
 * Description:
 * 爬虫测试
 */
@SpringBootTest
@Slf4j
class CrawlerTest {

    @Resource
    private PostService postService;


    @Test
    void testFetchPicture() throws IOException {
        int current = 1;
        String url = "https://www.bing.com/images/search?q=小黑子&first=" + current;
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".iuscp.isv");
        List<PictureEntity> pictureEntities = Lists.newArrayList();
        for (Element element : elements) {
            // 获取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            log.info("图片地址：{}", murl);
            String title = element.select(".inflnk").get(0).attr("aria-label");
            log.info("图片标题：{}", title);
            PictureEntity pictureEntity = new PictureEntity().setUrl(murl).setTitle(title);
            pictureEntities.add(pictureEntity);
        }
    }

    @Test
    void testFetchPassage() {
        // 1.获取数据
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest.post(url)
                .body(json)
                .execute()
                .body();
        // 2.json 转对象 （转对象时记得对请求判断）
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        Integer code =(Integer) map.get("code");
        List<PostEntity> postList = Lists.newArrayList();
        if (null != code && code == 0) {
            JSONObject data =(JSONObject) map.get("data");
            JSONArray records = (JSONArray) data.get("records");
            for (Object record : records) {
                JSONObject tempRecord = (JSONObject) record;
                PostEntity postEntity = new PostEntity();
                postEntity.setTitle(tempRecord.getStr("title"));
                postEntity.setContent(tempRecord.getStr("content"));
                JSONArray tags = (JSONArray) tempRecord.get("tags");
                List<String> strings = tags.toList(String.class);
                String tagJson = JSONUtil.toJsonStr(strings);
                postEntity.setTags(tagJson);
                postEntity.setUserId(1L);
                postList.add(postEntity);
            }
        }
        boolean b = postService.saveBatch(postList);
        Assertions.assertTrue(b);
    }
}
