package org.barren.tools.oss.config;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.barren.tools.oss.AliossTemplate;
import org.barren.tools.oss.MinioTemplate;
import org.barren.tools.oss.QiniuossTemplate;
import org.barren.tools.oss.props.OssProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * oss对象存储服务配置
 *
 * @author cxs
 **/
@Slf4j
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties({OssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "enabled", havingValue = "true")
public class OssConfiguration {

    private final OssProperties properties;

    //    @Bean
    //    @ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "name", havingValue = "aliyun")
    //    @ConditionalOnMissingBean
    //    public AliSmsTemplate aliSmsTemplate(OssProperties properties) {
    //        IClientProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getAccessKey(), smsProperties.getSecretKey());
    //        IAcsClient acsClient = new DefaultAcsClient(profile);
    //        return new AliSmsTemplate(smsProperties, acsClient);
    //    }
    //
    //    @Bean
    //    @ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "name", havingValue = "qiniu")
    //    @ConditionalOnMissingBean
    //    public QiniuSmsTemplate qiniuSmsTemplate(OssProperties properties) {
    //        Auth auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
    //        SmsManager smsManager = new SmsManager(auth);
    //        return new QiniuSmsTemplate(smsProperties, smsManager);
    //    }
    //
    //    @Bean
    //    @ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "name", havingValue = "tencent")
    //    @ConditionalOnMissingBean
    //    public TencentSmsTemplate tencentSmsTemplate(SmsProperties properties) {
    //        SmsMultiSender smsSender = new SmsMultiSender(NumberUtils.toInt(smsProperties.getAccessKey()), smsProperties.getSecretKey());
    //        return new TencentSmsTemplate(smsProperties, smsSender);
    //    }

    @Bean
    @ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "name", havingValue = "aliyun")
    @ConditionalOnMissingBean
    public AliossTemplate aliossTemplate() {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setMaxConnections(1024);
        conf.setSocketTimeout(50000);
        conf.setConnectionTimeout(50000);
        conf.setConnectionRequestTimeout(1000);
        conf.setIdleConnectionTime(60000L);
        conf.setMaxErrorRetry(5);
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(
                properties.getAccessKey(),
                properties.getSecretKey());
        OSSClient ossClient = new OSSClient(properties.getEndpoint(), credentialsProvider, conf);
        return new AliossTemplate(ossClient, properties);
    }

    @Bean
    @ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "name", havingValue = "minio")
    @ConditionalOnMissingBean
    public MinioTemplate minioTemplate() {
        MinioClient minioClient = MinioClient.builder().endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey()).build();
        return new MinioTemplate(minioClient, properties);
    }

    @Bean
    @ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "name", havingValue = "qiniu")
    @ConditionalOnMissingBean
    public QiniuossTemplate qiniuossTemplate() {
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Region.autoRegion());
        Auth auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
        UploadManager uploadManager = new UploadManager(cfg);
        BucketManager bucketManager = new BucketManager(Auth.create(properties.getAccessKey(), properties.getSecretKey()), cfg);
        return new QiniuossTemplate(auth, uploadManager, bucketManager, properties);
    }
}
