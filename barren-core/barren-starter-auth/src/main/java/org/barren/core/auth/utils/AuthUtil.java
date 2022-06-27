package org.barren.core.auth.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Objects;

/**
 * 解析JWT token获取用户信息
 */
@NoArgsConstructor
public class AuthUtil {

    private static String key;

    public AuthUtil(String key) {
        AuthUtil.key = key;
    }

    public void setKey(String key) {
        AuthUtil.key = key;
    }

    public String getKey() {
        return AuthUtil.key;
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return UserInfo
     */
    public static UserInfo getCurrentUser() {
        return getUserInfo().toJavaObject(UserInfo.class);
    }

    /***
     * 获取token中的用户信息json形式
     * @return
     */
    public static JSONObject getUserInfo() {
        //获取授权信息
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        //令牌解码
        return decode(details.getTokenValue());
    }

    public static JSONObject decode(String token) {
        return decode(token, AuthUtil.key);
    }

    /***
     * 读取令牌数据
     * @param token jwt
     * @param key 秘钥，默认对称加密算法秘钥
     * @return
     */
    public static JSONObject decode(String token, String key) {
        Objects.requireNonNull(key);
        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new MacSigner(key));
        //获取Jwt原始内容
        String claims = jwt.getClaims();
        return JSON.parseObject(claims);
    }

}


