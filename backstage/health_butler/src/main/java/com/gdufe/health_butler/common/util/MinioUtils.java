package com.gdufe.health_butler.common.util;

import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.policy.PolicyType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: laichengfeng
 * @Description: MinioUtils
 * @Date: 2019/3/5 10:18
 */
public class MinioUtils {

    private static Logger logger = LoggerFactory.getLogger(MinioUtils.class.getName());

    private static void createBucket(MinioClient minioClient, String bucket) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidObjectPrefixException {
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(bucket);
        if(!isExist) {
            // 创建一个存储桶，用于存储文件。
            minioClient.makeBucket(bucket);
        }
//        // 设置桶策略
//        minioClient.setBucketPolicy(bucket, "files", PolicyType.READ_WRITE);
    }


    /**
     * 上传文件
     * @param minioClient
     *          minio客户端
     * @param bucket
     *          桶
     * @param fileUrl
     *          文件源地址
     * @return
     */
    public static String FileUploaderByUrl(MinioClient minioClient, String bucket, String fileUrl) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidArgumentException, InvalidObjectPrefixException {
        logger.info("[op: FileUploaderByUrl, bucket:{}, fileUrl:{}]", bucket, fileUrl);
        createBucket(minioClient, bucket);
        // 得到文件类型
        String fileType = getFileType(fileUrl);
        // 使用MD5生成一个新的文件名（防止碰撞）
        String newFileName = DigestUtils.md5Hex(fileUrl) + fileType;
        // 使用putObject上传一个文件到存储桶中。
        minioClient.putObject(bucket, newFileName, fileUrl);
        // 返回新路径
        String newFileUrl = minioClient.getObjectUrl(bucket, newFileName);
        logger.info("[op_rslt: success, newFileUrl:{}]", newFileUrl);
        return newFileUrl;
    }

    /**
     * 文件上传
     * @param minioClient
     *          minio客户端
     * @param bucket
     *          桶
     * @param file
     *          源文件
     * @return
     */
    public static String FileUploaderByFile(MinioClient minioClient, String bucket, File file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidArgumentException, InvalidObjectPrefixException {
        logger.info("[op: FileUploaderByFile, bucket:{}, filePath:{}]", bucket, file.getAbsolutePath());
        createBucket(minioClient, bucket);
        String fileName = file.getName();
        String fileType = getFileType(fileName);
        String newFileName = DigestUtils.md5Hex(file.getAbsolutePath()) + fileType;
        // 默认使用multipart/form-data
        minioClient.putObject(bucket, newFileName, new FileInputStream(file), fileType.replaceAll(".", ""));
        String newFileUrl = minioClient.getObjectUrl(bucket, newFileName);
        logger.info("[op_rslt: success, newFileUrl:{}]", newFileUrl);
        return newFileUrl;
    }

    /**
     * 文件上传
     * @param minioClient
     *          minio客户端
     * @param bucket
     *          桶
     * @param stream
     *          流
     * @return
     */
    public static String FileUploaderByStream(MinioClient minioClient, String bucket, InputStream stream, String originFilePath)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidArgumentException, InvalidObjectPrefixException {
        logger.info("[op: FileUploaderByStream, bucket:{}, originPath:{}]", bucket, originFilePath);
        createBucket(minioClient, bucket);
        String fileName = originFilePath;
        String fileType = getFileType(fileName);
        String newFileName = DigestUtils.md5Hex(originFilePath) + fileType;
        // 默认使用multipart/form-data
        minioClient.putObject(bucket, newFileName, stream, fileType.replaceAll(".", ""));
        String newFileUrl = minioClient.getObjectUrl(bucket, newFileName);
        logger.info("[op_rslt: success, newFileUrl:{}]", newFileUrl);
        return newFileUrl;
    }

    public static String getFileType(String fileName) {
        return fileName!=null && fileName.contains(".")?fileName.substring(fileName.lastIndexOf(".")):"";
    }
}
