package org.barren.tools.oss.rule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;

/**
 * @author cxs
 **/
public class BaseOssRule implements OssRule {

    @Override
    public String bucketName(String bucketName) {
        return bucketName;
    }

    @Override
    public String fileName(String originalFilename) {
        return "upload/" + DateUtil.today() + "/" + UUID.randomUUID() + "." + FileUtil.extName(originalFilename);
    }

}
