package org.barren.core.web.controller;

import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.barren.core.tool.http.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 接口幂等controller，返回幂等token
 *
 * @author cxs
 */
@Slf4j
@RestController
@RequestMapping("/id-generator")
@RequiredArgsConstructor
@Api(tags = "系统：生成幂等令牌token")
public class IdGeneratorController {

    public static final String IDEMPOTENT_KEY_PREFIX = "IDEMPOTENT-KEY:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/token")
    public R<String> token() {
        String id = IdUtil.fastSimpleUUID();
        String key = IDEMPOTENT_KEY_PREFIX + id;
        redisTemplate.opsForValue().set(key, 1, 10L, TimeUnit.MINUTES);
        return R.ok(id);
    }
}
