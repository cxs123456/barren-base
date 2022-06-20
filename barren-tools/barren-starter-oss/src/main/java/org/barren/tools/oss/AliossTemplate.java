package org.barren.tools.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.barren.tools.oss.model.OssFile;
import org.barren.tools.oss.props.OssProperties;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author cxs
 **/
@Slf4j
@Data
@AllArgsConstructor
public class AliossTemplate implements OssTemplate {
    private OSSClient ossClient;
    private OssProperties ossProperties;

    @Override
    public void makeBucket(String bucketName) {
        ossClient.createBucket(bucketName);
    }

    @Override
    public void removeBucket(String bucketName) {
        ossClient.deleteBucket(bucketName);
    }

    @Override
    public boolean bucketExists(String bucketName) {
        return ossClient.doesBucketExist(bucketName);
    }

    @Override
    @SneakyThrows(Exception.class)
    public OssFile putFile(String bucketName, String fileName, MultipartFile file) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = ossProperties.getBucketName();
        }
        String originalName = fileName;
        fileName = getFilePath(fileName);
        PutObjectResult response = ossClient.putObject(bucketName, fileName, file.getInputStream());

        OssFile ossFile = new OssFile();
        ossFile.setOriginalName(originalName);
        ossFile.setName(fileName);
        ossFile.setDomain(getOssHost(bucketName));
        ossFile.setLink(fileLink(bucketName, fileName));
        return ossFile;
    }


    @Override
    public OssFile statFile(String bucketName, String fileName) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = ossProperties.getBucketName();
        }
        ObjectMetadata stat = ossClient.getObjectMetadata(bucketName, fileName);
        OssFile ossFile = new OssFile();
        ossFile.setName(fileName);
        ossFile.setLink(fileLink(fileName));
        ossFile.setHash(stat.getContentMD5());
        ossFile.setLength(stat.getContentLength());
        ossFile.setPutTime(stat.getLastModified());
        ossFile.setContentType(stat.getContentType());
        return ossFile;

    }

    @Override
    public String fileLink(String bucketName, String fileName) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = ossProperties.getBucketName();
        }
        return getOssHost(bucketName).concat("/").concat(fileName);
    }

    @Override
    public void removeFile(String bucketName, String fileName) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = ossProperties.getBucketName();
        }
        ossClient.deleteObject(bucketName, fileName);
    }

    public String getOssHost(String bucketName) {
        String prefix = ossProperties.getEndpoint().contains("https://") ? "https://" : "http://";
        return prefix + bucketName + "." + ossProperties.getEndpoint().replaceFirst(prefix, "");
    }

    public String getOssHost() {
        return getOssHost(ossProperties.getBucketName());
    }

}
