package com.christer.project.util;

import cn.hutool.core.date.DateUtil;
import com.christer.project.common.ResultCode;
import com.christer.project.config.COSConfig;
import com.christer.project.exception.BusinessException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.Headers;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static cn.hutool.extra.spring.SpringUtil.getBean;
import static com.christer.project.common.ResultCode.SYSTEM_ERROR;
import static com.christer.project.constant.RedisConstant.SIGNATURE;
import static com.christer.project.constant.RedisConstant.SIGNATURE_TTL;

/**
 * @author Christer
 * @version 1.0
 * @date 2023-09-04 23:06
 * Description:
 * 腾讯云COS文件上传工具类
 */
@Slf4j
@Component
public class COSClientUtils {

    /**
     * 获取配置信息
     */
    private static final COSConfig cosConfig = getBean(COSConfig.class);

    /**
     * 初始化用户身份信息
     */
    private static final COSCredentials cred = new BasicCOSCredentials(cosConfig.getAccessKey(), cosConfig.getSecretKey());

    /**
     * 设置bucket的区域
     */
    private static final ClientConfig clientConfig = new ClientConfig(new Region(cosConfig.getRegionName()));

    /**
     * 生成COS客户端
     */
    private static final COSClient cosClient = new COSClient(cred, clientConfig);

    /**
     * redis 工具类
     */
    private static RedisUtil redisUtil;

    private COSClientUtils() {

    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        COSClientUtils.redisUtil = redisUtil;
    }


    /**
     * 获取腾讯云cos 的签名
     *
     * @return 签名的url
     */
    public static String getCosSign(String fileType) {
        // 上传类型为 图片: image 视频： video
        // 上传文件，请使用PUT方法
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(cosConfig.getBucketName(), fileType, HttpMethodName.PUT);
        // 设置签名过期时间(可选), 若未进行设置则默认使用 ClientConfig 中的签名过期时间(1小时)
        // 这里设置签名在30min后过期
        Date expirationDate = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
        req.setExpiration(expirationDate);
        // 填写本次请求的头部。Host 必填
        req.putCustomRequestHeader(Headers.HOST,
                cosClient.getClientConfig().getEndpointBuilder()
                        .buildGeneralApiEndpoint(cosConfig.getBucketName()));
        // 获取预签名URL
        URL url = cosClient.generatePresignedUrl(req);
        // 生成签名的key
        String signKey = SIGNATURE + UUID.randomUUID().toString().replace("-", "");
        // 将签名放到redis中
        redisUtil.setEx(signKey, url.toString(), SIGNATURE_TTL, TimeUnit.MINUTES);
        return signKey;
    }

    /**
     * 上传文件
     *
     * @param file    文件流
     * @param signKey 签名的key
     * @return void
     * @throws Exception 文件上传异常
     */
    public static String upload(MultipartFile file, String signKey) throws Exception {
        String date = DateUtil.today();
        String originalFilename = file.getOriginalFilename();
        if (log.isDebugEnabled()) {
            log.debug("文件名称：{}", originalFilename);
        }
        // 用uuid生成唯一id
        String nextId = UUID.randomUUID().toString().replace("-", "");
        assert originalFilename != null;
        String name = nextId + originalFilename.substring(originalFilename.lastIndexOf("."));
        String folderName = cosConfig.getFilePrefix() + "/" + date + "/";
        if (log.isDebugEnabled()) {
            log.debug("folderName:{}", folderName);
        }
        String key = folderName + name;
        if (log.isDebugEnabled()) {
            log.debug("key:{}", key);
        }
        Path localFilePath = null;
        try {
            localFilePath = transferToFile(file).toPath();
            String filePath = uploadFileToCOS(localFilePath.toFile(), key, signKey);
            log.info("upload COS successful: {}", filePath);
            return filePath;
        } catch (InterruptedException e) {
            // 发生异常，中断当前线程
            Thread.currentThread().interrupt();
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR, "文件上传失败!", e);
        } finally {
            if (localFilePath != null) {
                try {
                    Files.delete(localFilePath);
                } catch (
                        IOException e) {
                    log.error("Failed to delete local file {}: {}", localFilePath, e.getMessage());
                }
            }
        }
    }

    /**
     * 上传文件到COS
     *
     * @param localFile
     * @param key
     * @return
     */
    private static String uploadFileToCOS(File localFile, String key, String signKey) throws InterruptedException {
        // 从redis 中获取签名
        String sign = Optional.ofNullable((String) redisUtil.getObjectByKey(signKey))
                .orElseThrow(() -> new BusinessException(SYSTEM_ERROR.getCode(), "签名已过期"));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置上传文件的请求签名
        objectMetadata.setHeader("Authorization", sign);
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosConfig.getBucketName(), key, localFile);
        putObjectRequest.setMetadata(objectMetadata);
        // 创建一个线程池，利用线程池使用分片上传
        ExecutorService threadPool = Executors.newFixedThreadPool(8);
        // 传入一个threadPool, 若不传入线程池, 默认TransferManager中会生成一个单线程的线程池
        TransferManager transferManager = new TransferManager(cosClient, threadPool);
        // 返回一个异步结果Upload, 可同步的调用waitForUploadResult等待upload结束, 成功返回UploadResult, 失败抛出异常
        final Upload upload = transferManager.upload(putObjectRequest);
        if (log.isDebugEnabled()) {
            log.debug("upload:{}", upload);
        }
        UploadResult uploadResult = upload.waitForUploadResult();
        final String filePath = cosConfig.getBaseUrl() + uploadResult.getKey();
        // 不要立即关闭COSClient，等程序结束时才关闭
        transferManager.shutdownNow(false);
//        cosClient.shutdown();
        return filePath;
    }

    /**
     * 用缓冲区来实现这个转换, 即创建临时文件
     * 使用 MultipartFile.transferTo()
     *
     * @param multipartFile
     * @return
     */
    private static File transferToFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        assert originalFilename != null;
        String prefix = originalFilename.split("\\.")[0];
        // 文件名名称长度小于3,在创建文件时会抛出异常，需要注意
        if (prefix.length() < 3) {
            prefix += "__";
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        File file = File.createTempFile(prefix, suffix);
        multipartFile.transferTo(file);
        return file;
    }

}