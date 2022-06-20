package org.barren.tools.oss;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import org.barren.tools.oss.model.OssFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author cxs
 **/
public interface OssTemplate {

    void makeBucket(String bucketName);

    void removeBucket(String bucketName);

    boolean bucketExists(String bucketName);

    default OssFile putFile(MultipartFile file) {
        return putFile(file.getOriginalFilename(), file);
    }

    default OssFile putFile(String fileName, MultipartFile file) {
        return putFile(null, fileName, file);
    }

    OssFile putFile(String bucketName, String fileName, MultipartFile file);


    /**
     * 获取文件详情
     *
     * @param fileName
     * @return
     */
    default OssFile statFile(String fileName) {
        return statFile(null, fileName);
    }

    OssFile statFile(String bucketName, String fileName);

    /**
     * 返回文件链接
     *
     * @param fileName
     * @return
     */
    default String fileLink(String fileName) {
        return fileLink(null, fileName);
    }

    String fileLink(String bucketName, String fileName);

    default void removeFile(String fileName) {
        removeFile(null, fileName);
    }

    default void removeFiles(List<String> fileNames) {
        fileNames.forEach(this::removeFile);
    }

    default void removeFiles(String bucketName, List<String> fileNames) {
        fileNames.forEach((fileName) -> {
            removeFile(bucketName, fileName);
        });
    }

    void removeFile(String bucketName, String fileName);


    default String getFilePath(String originalFilename) {
        return makeFileUploadPath(originalFilename);
    }

    /**
     * 生成文件上传地址
     *
     * @param originalFilename 上传文件原名
     * @return
     */
    static String makeFileUploadPath(String originalFilename) {
        return "upload/" + DateUtil.today() + "/" + UUID.randomUUID() + "." + FileUtil.extName(originalFilename);
    }
}
