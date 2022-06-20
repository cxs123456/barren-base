package org.barren.core.auth.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author cxs
 **/
@Data
@ConfigurationProperties(prefix = AuthProperties.PREFIX)
public class AuthProperties {
    public static final String PREFIX = "auth";
    /**
     * hmac加密，秘钥
     */
    private String secretKey = "barren";

    private List<Client> clients = Stream.of(new Client("app", "barren", "all")).collect(Collectors.toList());

    private LoginCode loginCode = new LoginCode();

    /**
     * oauth2 客户端配置
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Client {

        private String clientId;

        private String clientSecret;

        private String scope = "all";
    }

    /**
     * 登录图片验证码配置
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginCode {

        /**
         * 验证码存在Redis中key前缀
         */
        private String codeKey = "barren-captcha:";
        /**
         * 验证码有效期 分钟
         */
        private int expire = 2;
        /**
         * 验证码内容长度
         */
        private int length = 2;
        /**
         * 验证码宽度
         */
        private int width = 111;
        /**
         * 验证码高度
         */
        private int height = 36;
        /**
         * 验证码字体
         */
        private String fontName;
        /**
         * 字体大小
         */
        private int fontSize = 25;
    }
}
