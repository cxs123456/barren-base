package org.barren.tools.oss;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
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
public class QiniuossTemplate implements OssTemplate {

    private Auth auth;
    private UploadManager uploadManager;
    private BucketManager bucketManager;
    private OssProperties ossProperties;

    @Override
    @SneakyThrows
    public void makeBucket(String bucketName) {
        bucketManager.createBucket(bucketName, Zone.autoZone().getRegion());
    }

    @Override
    public void removeBucket(String bucketName) {
    }

    @Override
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        return ArrayUtils.contains(bucketManager.buckets(), bucketName);
    }

    @Override
    @SneakyThrows(Exception.class)
    public OssFile putFile(String bucketName, String fileName, MultipartFile file) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = ossProperties.getBucketName();
        }
        InputStream inputStream = file.getInputStream();
        String originalName = fileName;
        fileName = getFilePath(fileName);
        uploadManager.put(inputStream, fileName, auth.uploadToken(bucketName, fileName), null, null);
        OssFile ossFile = new OssFile();
        ossFile.setOriginalName(originalName);
        ossFile.setName(fileName);
        ossFile.setDomain(getOssHost());
        ossFile.setLink(fileLink(bucketName, fileName));
        return ossFile;
    }


    @Override
    @SneakyThrows(Exception.class)
    public OssFile statFile(String bucketName, String fileName) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = ossProperties.getBucketName();
        }
        FileInfo stat = this.bucketManager.stat(bucketName, fileName);

        OssFile ossFile = new OssFile();
        ossFile.setName(Objects.toString(stat.key, fileName));
        ossFile.setLink(fileLink(fileName));
        ossFile.setHash(stat.hash);
        ossFile.setLength(stat.fsize);
        ossFile.setPutTime(new Date(stat.putTime / 10000L));
        ossFile.setContentType(stat.mimeType);
        return ossFile;

    }

    @Override
    public String fileLink(String bucketName, String fileName) {
        return ossProperties.getEndpoint().concat("/").concat(fileName);
    }

    @Override
    @SneakyThrows(Exception.class)
    public void removeFile(String bucketName, String fileName) {
        if (StringUtils.isBlank(bucketName)) {
            bucketName = ossProperties.getBucketName();
        }
        bucketManager.delete(bucketName, fileName);
    }

    public String getOssHost() {
        return ossProperties.getEndpoint();
    }

}
