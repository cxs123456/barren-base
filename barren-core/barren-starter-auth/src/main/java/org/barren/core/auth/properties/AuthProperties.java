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

    /**
     * 配置 oauth2 客户端 clientId和clientSecret
     */
    private List<Client> clients = Stream.of(new Client("app", "barren", "all")).collect(Collectors.toList());

    /**
     * 配置 登录页面 图片验证码
     */
    private LoginCode loginCode = new LoginCode();

    /**
     * 配置 jwt Token续期
     */
    private JwtConfig jwtConfig = new JwtConfig();


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

    /**
     * jwt Token续期配置
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JwtConfig {

        /**
         * 令牌过期时间，默认2小时，单位秒
         */
        private int tokenValidSeconds = 2 * 60 * 60;

        /**
         * 刷新令牌过期时间，默认2小时，单位秒
         */
        private int refreshTokenValidSeconds = 3 * 60 * 60;

        /**
         * token 续期检查时间范围（默认20分钟，单位秒），在token即将过期的一段时间内用户操作了，则给用户的token续期
         */
        private int detect = 1200;
        /**
         * 续期时间范围，默认2小时，单位秒
         */
        private int renew = 7200;

        /**
         * 前端 token 保存在cookie中的key
         */
        private String tokenKey = "Admin-Token";
    }
}
