package com.christer.project.model.enums;

import org.springframework.util.ObjectUtils;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-12-13 23:23
 * Description:
 * 搜索类型枚举
 */
public enum SearchTypeEnum {

    USER("用户", "user"),
    POST("帖子", "post"),
    PICTURE("图片", "picture");

    private final String text;

    private final String value;

    SearchTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }
    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static SearchTypeEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (SearchTypeEnum anEnum : SearchTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
