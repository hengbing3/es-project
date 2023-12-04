package com.christer.project;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.christer.project.model.entity.post.PostEntity;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
class CrawlerTest {

    @Test
    void test() {
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


        System.out.println(postList);
    }
}
