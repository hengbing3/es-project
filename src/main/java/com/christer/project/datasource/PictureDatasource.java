package com.christer.project.datasource;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.christer.project.common.ResultCode;
import com.christer.project.exception.BusinessException;
import com.christer.project.model.entity.picture.PictureEntity;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-06 22:46
 * Description:
 */
@Service
public class PictureDatasource implements DataSource<PictureEntity> {


    @Override
    public Page<PictureEntity> doSearch(String searchText, long pageNum, long pageSize) {
        int currentPage = (int) ((pageNum - 1) * pageSize);
        String url = String.format("https://www.bing.com/images/search?q=%s&first=%s", searchText, currentPage);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "获取图片失败！");
        }
        Elements elements = doc.select(".iuscp.isv");
        List<PictureEntity> pictureEntities = Lists.newArrayList();
        for (Element element : elements) {
            if (pictureEntities.size() >= pageSize ) {
                break;
            }
            // 获取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            String title = element.select(".inflnk").get(0).attr("aria-label");
            PictureEntity pictureEntity = new PictureEntity().setUrl(murl).setTitle(title);
            pictureEntities.add(pictureEntity);
        }
        Page<PictureEntity> entityPage = new Page<>(pageNum, pageSize);
        entityPage.setRecords(pictureEntities);
        entityPage.setTotal(pictureEntities.size());
        return entityPage;
    }
}
