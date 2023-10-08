package com.christer.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-09-04 22:16
 * Description:
 * 腾讯云cos桶配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "cos")
public class COSConfig {
    /**
     * 访问域名
     */
    private String baseUrl;
    /**
     * 访问密钥
     */
    private String accessKey;
    /**
     * 访问密码
     */
    private String secretKey;
    /**
     * 地域名称
     */
    private String regionName;
    /**
     * 桶名称
     */
    private String bucketName;
    /**
     * 图片文件前缀
     */
    private String imagePrefix;

    /**
     * 视频文件前缀
     */
    private String videoPrefix;
    /**
     * 文件前缀
     */
    private String filePrefix;
}
