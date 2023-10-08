package com.christer.project.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-09-04 23:04
 * Description:
 * Redis操作的工具类
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * redis setEx
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    public void setEx(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    /**
     * redis getAndDelete
     *
     * @param key 键
     * @return 值
     */
    public Object getAndDelete(String key) {
        return redisTemplate.opsForValue().getAndDelete(key);
    }

    /**
     * redis get
     *
     * @param key 键
     * @return 获取对象
     */
    public Object getObjectByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
