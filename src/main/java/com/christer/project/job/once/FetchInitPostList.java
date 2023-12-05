package com.christer.project.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.christer.project.model.entity.post.PostEntity;
import com.christer.project.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-05 22:06
 * Description:
 * 获取初始化的帖子列表 (取消 Component 注释后，每次启动Springboot项目,会执行一次run方法)
 */
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;


    @Override
    public void run(String... args) throws Exception {
        // 1.获取数据
        String json = "{\"current\":2,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
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
            for (Object myRecord : records) {
                JSONObject tempRecord = (JSONObject) myRecord;
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
        // 批量插入帖子
        boolean b = postService.saveBatch(postList);
        if (b) {
            log.info("初始化帖子列表成功，条数:{}", postList.size());
        } else {
            log.error("初始化帖子列表失败！");
        }
    }
}
