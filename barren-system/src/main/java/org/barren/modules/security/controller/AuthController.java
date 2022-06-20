package org.barren.modules.security.controller;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.barren.core.auth.properties.AuthProperties;
import org.barren.core.tool.http.R;
import org.barren.modules.security.entity.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 获取图片验证码、用户信息
 *
 * @author cxs
 **/
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "系统：系统认证授权接口")
public class AuthController {

    @Autowired
    private AuthProperties properties;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AuthProperties authProperties;
    @Value("${server.port}")
    private int port;

    @ApiOperation("登录授权")
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public R<Object> login(LoginRequest loginRequest) {
        String codeKey = loginRequest.getCodeKey();
        String num = loginRequest.getNum();
        String val = redisTemplate.opsForValue().get(codeKey);
        if (StringUtils.isBlank(val)) {
            return R.fail("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(num) || !val.equals(num)) {
            return R.fail("验证码错误");
        }

        String username = loginRequest.getUsername();
        // todo 这里密码需要前后端 RSA加密传输
        String password = loginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            return R.fail("用户名或密码错误");
        }
        String clientId = authProperties.getClients().get(0).getClientId();
        String clientSecret = authProperties.getClients().get(0).getClientSecret();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        body.set("grant_type", "password");
        body.set("username", username);
        body.set("password", password);
        header.set("Authorization", "Basic " + getAuthorization(clientId, clientSecret));
        // 由于oauth2 请求接口不能自定逻辑，无法接收额外参数处理额外逻辑，必须自定义认证接口去调用oauth获取token接口，添加自定义逻辑
        String url = "http://localhost:" + port + "/oauth/token";
        ResponseEntity<Map> response;
        try {
            // 用户名或密码错误，这里会出现异常
            response = restTemplate.postForEntity(url, new HttpEntity<>(body, header), Map.class);
            // 请求转发，返回值处理很麻烦
            // request.getRequestDispatcher("/oauth/token").forward(request, response);
        } catch (Exception e) {
            log.error(" login error: ", e);
            return R.fail(null, e.getMessage());
        }
        return R.ok(response.getBody());
    }


    @ApiOperation("获取图片验证码")
    @RequestMapping(value = "/captcha", method = {RequestMethod.GET, RequestMethod.POST})
    public R<Map<String, Object>> captcha(HttpServletRequest request, HttpServletResponse response) {
        // 获取用户ip地址
        // 使用算术验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        // 设置code key
        String codeKey = properties.getLoginCode().getCodeKey() + IdUtil.simpleUUID();
        // 获取运算的结果
        String captchaValue = captcha.text();
        // 保存到Redis，默认保留2分钟
        redisTemplate.opsForValue().set(codeKey, captchaValue, properties.getLoginCode().getExpire(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("codeKey", codeKey);
        }};
        return R.ok(imgResult);
    }

    // @ApiOperation("获取用户信息")
    // @GetMapping(value = "/info")
    // public R<Object> getUserInfo() {
    //     return ResponseEntity.ok(SecurityUtils.getCurrentUser());
    // }

    /**
     * 请求头的信息处理
     *
     * @param clientId
     * @param clientSecret
     * @return
     */
    public String getAuthorization(String clientId, String clientSecret) {
        String result = clientId + ":" + clientSecret;
        byte[] bytes = Base64.getEncoder().encode(result.getBytes());
        return new String(bytes);
    }
}
