package org.soho.common.service;

import io.minio.*;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soho.common.exception.BusinessException;
import org.soho.common.model.enums.ErrorCode;
import org.soho.common.util.DateUtil;
import org.soho.common.util.MessageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wesoho
 * @version 1.0
 * @description: minio文件处理
 * @date 2024/10/27 11:11
 */
@Service
public class MinioService {
    private static final Logger log = LoggerFactory.getLogger(MinioService.class);
    @Resource
    private MinioClient minioClient;

    @Getter
    @Value("${minio.bucketName}")
    private String bucketName;

    @Resource
    private MessageUtil messageUtil;

    public void uploadAvatar(MultipartFile avatar, String username) throws IOException {
        MultipartFile avatarFile = avatar;
        // 生成文件名
        String uniqueFileName = generateUniqueFileName(avatarFile.getOriginalFilename(), username);
        // 上传文件
        uploadFile(avatarFile.getInputStream(), avatarFile.getContentType(), uniqueFileName, getBucketName());
    }

    /**
     * 文件名称
     *
     * @param inputStream 文件流
     * @param objectName  路径和名称，唯一
     * @param bucketName  桶名称
     * @return
     */
    @SneakyThrows
    public ObjectWriteResponse uploadFile(InputStream inputStream, String contentType, String objectName, String bucketName) {
        if (inputStream == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, messageUtil.getMessage("exception.file.null"));
        }
        if (!bucketExists(bucketName)) {
            makeBucket(bucketName);
        }
        return minioClient.putObject(PutObjectArgs.builder().contentType(contentType).object(objectName).bucket(bucketName).stream(inputStream, inputStream.available(), -1).build());
    }

    /**
     * 下载文件
     *
     * @param bucketName 桶名称
     * @param objectName 文件名
     */
    public InputStream download(String bucketName, String objectName) {
        try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build())) {
            return inputStream;
        } catch (Exception e) {
            log.error(messageUtil.getMessage("exception.file.download"), e);
            return new ByteArrayInputStream(new byte[0]);
        }
    }

    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    public void makeBucket(String bucketName) throws Exception {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    // 生成唯一文件名
    public String generateUniqueFileName(String originalFileName, String username) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String nowTimestamp = DateUtil.now("yyyyMMddHHmmss");
        String uniqueFileName = username + "_" + nowTimestamp + fileExtension;
        return uniqueFileName;
    }
}
