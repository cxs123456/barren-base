package org.barren.tools.oss;

import io.minio.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.barren.tools.oss.model.OssFile;
import org.barren.tools.oss.props.OssProperties;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.Objects;

/**
 * @author cxs
 **/
@Slf4j
@Data
@AllArgsConstructor
public class MinioTemplate implements OssTemplate {

    private final MinioClient client;
    private final OssProperties ossProperties;

    @Override
    @SneakyThrows
    public void makeBucket(String bucketName) {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    @Override
    @SneakyThrows
    public void removeBucket(String bucketName) {
        client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    @Override
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        return client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    @Override
    @SneakyThrows(Exception.class)
    public OssFile putFile(String bucketName, String fileName, MultipartFile file) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = ossProperties.getBucketName();
        }
        String contentType = file.getContentType();
        InputStream inputStream = file.getInputStream();
        String originalName = fileName;
        fileName = getFilePath(fileName);
        client.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName)
                .stream(inputStream, inputStream.available(), -1L).contentType(contentType)
                .build());
        OssFile ossFile = new OssFile();
        ossFile.setOriginalName(originalName);
        ossFile.setName(fileName);
        ossFile.setDomain(this.getOssHost(bucketName));
        ossFile.setLink(fileLink(bucketName, fileName));
        return ossFile;
    }

    @Override
    @SneakyThrows(Exception.class)
    public OssFile statFile(String bucketName, String fileName) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = ossProperties.getBucketName();
        }
        StatObjectResponse stat = this.client.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
        OssFile ossFile = new OssFile();
        ossFile.setName(Objects.toString(stat.object(), fileName));
        ossFile.setLink(fileLink(ossFile.getName()));
        ossFile.setHash(String.valueOf(stat.hashCode()));
        ossFile.setLength(stat.size());
        ossFile.setPutTime(Date.from(stat.lastModified().toInstant()));
        ossFile.setContentType(stat.contentType());
        return ossFile;
    }

    @Override
    public String fileLink(String bucketName, String fileName) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = ossProperties.getBucketName();
        }
        return this.ossProperties.getEndpoint().concat("/").concat(bucketName).concat("/").concat(fileName);
    }

    @Override
    @SneakyThrows
    public void removeFile(String bucketName, String fileName) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = ossProperties.getBucketName();
        }
        this.client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }

    public String getOssHost(String bucketName) {
        return this.ossProperties.getEndpoint() + "/" + bucketName;
    }

    public String getOssHost() {
        return this.getOssHost(this.ossProperties.getBucketName());
    }

}
