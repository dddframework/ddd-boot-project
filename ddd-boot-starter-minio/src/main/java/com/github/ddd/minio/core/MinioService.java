package com.github.ddd.minio.core;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.minio.spring.boot.autoconfigure.MinioProperties;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Minio Service
 *
 * @author ranger
 */
@Slf4j
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;


    private final MinioProperties minioProperties;

    /**
     * 判断存储桶是否存在
     *
     * @param bucket bucket
     * @return boolean
     */
    public boolean bucketExists(String bucket) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        } catch (Exception e) {
            log.warn("minio server error", e);
            return false;
        }
    }

    /**
     * 创建存储桶
     *
     * @param bucket bucket
     * @return  boolean
     */
    public boolean makeBucket(String bucket) {
        try {
            if (bucketExists(bucket)) {
                return true;
            }
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            return true;
        } catch (Exception e) {
            log.warn("minio make bucket error", e);
            return false;
        }
    }

    /**
     * 删除存储桶
     *
     * @param bucket bucket
     * @return  boolean
     */
    public boolean removeBucket(String bucket) {
        try {
            if (bucketExists(bucket)) {
                return true;
            }
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
            return true;
        } catch (Exception e) {
            log.warn("minio remove bucket error", e);
            return false;
        }
    }

    /**
     * 文件上传 Base64模式
     *
     * @param bucket bucket
     * @param name name
     * @return url
     */
    public String uploadBase64(String base64, String bucket, String name) {
        ByteArrayOutputStream out = null;
        ByteArrayInputStream stream = null;
        try {
            out = new ByteArrayOutputStream();
            Base64.decodeToStream(base64, out, false);
            stream = new ByteArrayInputStream(out.toByteArray());

            return this.uploadStream(stream, bucket, name);
        } catch (Exception e) {
            log.error("minio upload error", e);
            throw new RuntimeException("minio upload error", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.error("minio close IOException", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("minio close IOException", e);
                }
            }
        }
    }

    /**
     * 文件上传 流模式
     *
     * @param stream stream
     * @param bucket bucket
     * @param name name
     * @return url
     */
    public String uploadStream(InputStream stream, String bucket, String name) {
        try {
            String contentType = FileUtil.getMimeType(name);
            PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    // 桶名称
                    .bucket(bucket)
                    .stream(stream, stream.available(), -1)
                    // 上传到Minio的文件名 重复的话会进行覆盖 如果是2021/11/04/xx.txt类的文件名 则会生成对应目录结构
                    .object(name);
            if (StrUtil.isNotBlank(contentType)) {
                // 如果不设置contentType 上传的诸如图片等文件将无法在Minio上预览
                builder.contentType(contentType);
            }
            PutObjectArgs args = builder.build();
            minioClient.putObject(args);
            return this.getFileUrl(bucket, name);
        } catch (Exception e) {
            log.error("minio upload error", e);
            throw new RuntimeException("minio upload error", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.error("minio close IOException", e);
                }
            }
        }
    }

    /**
     * 文件合并
     * 分片最小不能小于5M否则报错
     *
     * @param sources sources
     * @param bucket bucket
     * @param name name
     * @return url
     */
    public String mergeFile(List<ComposeSource> sources, String bucket, String name) {
        try {
            minioClient.composeObject(ComposeObjectArgs.builder()
                    .bucket(bucket)
                    .object(name)
                    .sources(sources)
                    .build()
            );
            return this.getFileUrl(bucket, name);
        } catch (Exception e) {
            log.error("minio mergeFile error", e);
            throw new RuntimeException("minio mergeFile error", e);
        }
    }

    /**
     * 获取文件永久链接
     *
     * @param bucketName bucketName
     * @param objectName objectName
     * @return url
     */
    public String getFileUrl(String bucketName, String objectName) {
        String prefix = minioProperties.getUrl();
        if (StrUtil.isNotBlank(minioProperties.getDomain())) {
            prefix = minioProperties.getDomain();
        }
        return prefix + "/" + bucketName + "/" + objectName;
    }


    /**
     * 获取文件分享URL
     * 注：.txt的附件会乱码，PDF正常
     * 注：默认有效期7天
     *
     * @param bucketName 桶名称
     * @param objectName 存储在文件服务器中的附件名称
     * @param expireTime 有效时间 单位是秒
     * @return url
     */
    public String getFileShareUrl(String bucketName, String objectName, int expireTime) {
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(expireTime)
                    .build());
        } catch (Exception e) {
            log.warn("minio getFileShareUrl error", e);
            return null;
        }
    }

    /**
     * 删除附件
     *
     * @param bucketName 桶名称
     * @param objectName 存储在文件服务器中的附件名称
     */
    public boolean removeFile(String bucketName, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        } catch (Exception e) {
            log.warn("bucketName: {},objectName:{} remove error...", bucketName, objectName, e);
            return false;
        }
    }

    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return StatObjectResponse
     */
    public StatObjectResponse statObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            try {
                return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
            } catch (Exception e) {
                log.warn("bucketName: {},objectName:{} statObject error...", bucketName, objectName, e);
                return null;
            }
        }
        return null;
    }

}
