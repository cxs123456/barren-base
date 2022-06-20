package org.barren.tools.sms.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * - 短信服务（Short Message Service）
 * 短信服务是广大企业客户快速触达手机用户所优选使用的通信能力。调用API或用群发助手，即可发送验证码、通知类和营销类短信。
 * - 短信模版（TemplateId）
 * 使用短信服务首先都需要创建短信模板提交审核，这样可以防止不法分子通过云服务商提供的短信服务实施短信诈骗。
 * - 短信签名（SignName）
 * 短信末尾会附上签名以识别此条短信是由谁发送，这样可以令用户对短信来源有一个明确的印象。
 * - 地域（RegionId）
 * 地域表示SMS的数据中心所在物理位置。可以根据费用、请求来源等选择合适的地域，一般是阿里云短信配置。
 * - 访问密钥（AccessKey）
 * AccessKey简称AK，指的是访问身份验证中用到的AccessKey ID和AccessKey Secret。SMS通过使用AccessKey ID和AccessKey Secret对称加密的方法来验证某个请求的发送者身份。
 * AccessKey ID用于标识用户；AccessKey Secret是用户用于加密签名字符串和SMS用来验证签名字符串的密钥，必须保密。关于获取AccessKey的方法
 *
 * @author cxs
 **/
@Data
@ConfigurationProperties(prefix = SmsProperties.PREFIX)
public class SmsProperties {
    public static final String PREFIX = "sms";

    private Boolean enabled;
    /**
     * 默认启用阿里云
     */
    private String name = "aliyun";
    private String templateId;
    private String regionId = "cn-shenzheng";
    private String accessKey;
    private String secretKey;
    private String signName;
}
