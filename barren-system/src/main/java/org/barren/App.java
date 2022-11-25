package org.barren;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.barren.core.auth.utils.AuthUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import org.barren.core.auth.utils.AuthUtil;

/**
 * note:
 *
 * @author cxs
 **/
@EnableScheduling
@EnableAsync
@SpringBootApplication
@MapperScan("org.barren.**.mapper")
@RestController
@RequestMapping("/demo")
@Slf4j
@Api(tags = "demo")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }


    @ApiOperation(value = "hello", notes = "hello")
    @GetMapping("/hello")
    public String hello(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String atoken = token.split(" ")[1];
        JSONObject decode = AuthUtil.decode(atoken);
        log.info("token json = {}", decode);

        JSONObject userInfo = AuthUtil.getUserInfo();
        return "hello serviceA AAA, you token = " + token + "\n" + userInfo;
    }

    @GetMapping("/anyone")
    public String anyone() {
        if (RandomUtil.randomInt(10) > 5) {
            throw new RuntimeException("zzz");
        }
        return "hello anyone, you need't token";
    }
}
