package com.christer.project.datasource;

import com.christer.project.model.enums.SearchTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-17 18:05
 * Description:
 * 数据源注册器
 */
@Component
@RequiredArgsConstructor
public class DataSourceRegistry {

    private final UserDataSource userDataSource;

    private final PostDataSource postDataSource;

    private final PictureDatasource pictureDataSource;

    private Map<String, DataSource<?>> typeDataSourcesMap;

    @PostConstruct
    public void init() {
        // 初始化时指定初始容量
        typeDataSourcesMap = new HashMap<>(3);
        typeDataSourcesMap.put(SearchTypeEnum.POST.getValue(), postDataSource);
        typeDataSourcesMap.put(SearchTypeEnum.USER.getValue(), userDataSource);
        typeDataSourcesMap.put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
    }

    public DataSource<?> getDataSourceByType(String type) {
        if (!StringUtils.hasText(type)) {
            return null;
        }
        return typeDataSourcesMap.get(type);
    }

}
