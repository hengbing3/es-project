package com.christer.project.constant;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-09-04 23:08
 * Description:
 * redis使用的常量
 */
public final class RedisConstant {

    private RedisConstant() {

    }

    /**
     * 验证码过期时间
     */
    public static final Long CAPTCHA_TTL = 2L;

    /**
     * 腾讯云cos 签名key前缀
     */
    public static final String SIGNATURE = "sign:";

    /**
     * 签名过期时间
     */
    public static final Long SIGNATURE_TTL = 30L;
}
