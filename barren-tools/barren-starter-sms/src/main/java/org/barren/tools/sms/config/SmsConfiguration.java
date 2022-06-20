package org.barren.tools.sms.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.github.qcloudsms.SmsMultiSender;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.barren.tools.sms.AliSmsTemplate;
import org.barren.tools.sms.QiniuSmsTemplate;
import org.barren.tools.sms.TencentSmsTemplate;
import org.barren.tools.sms.props.SmsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信服务配置
 *
 * @author cxs
 **/
@Slf4j
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties({SmsProperties.class})
@ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "enabled", havingValue = "true")
public class SmsConfiguration {

    private final SmsProperties properties;

    @Bean
    @ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "name", havingValue = "aliyun")
    @ConditionalOnMissingBean
    public AliSmsTemplate aliSmsTemplate(SmsProperties smsProperties) {
        IClientProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getAccessKey(), smsProperties.getSecretKey());
        IAcsClient acsClient = new DefaultAcsClient(profile);
        return new AliSmsTemplate(smsProperties, acsClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "name", havingValue = "qiniu")
    @ConditionalOnMissingBean
    public QiniuSmsTemplate qiniuSmsTemplate(SmsProperties smsProperties) {
        Auth auth = Auth.create(smsProperties.getAccessKey(), smsProperties.getSecretKey());
        SmsManager smsManager = new SmsManager(auth);
        return new QiniuSmsTemplate(smsProperties, smsManager);
    }

    @Bean
    @ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "name", havingValue = "tencent")
    @ConditionalOnMissingBean
    public TencentSmsTemplate tencentSmsTemplate(SmsProperties smsProperties) {
        SmsMultiSender smsSender = new SmsMultiSender(NumberUtils.toInt(smsProperties.getAccessKey()), smsProperties.getSecretKey());
        return new TencentSmsTemplate(smsProperties, smsSender);
    }
}
