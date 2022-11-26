package org.barren;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Spring-Boot整合JUnit5的单元测试
 *
 * @author cxs
 * @date 2021/12/3 12:19
 **/
//@WebMvcTest // 分层测试方法
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DemoTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testHello() {
        String requestResult = this.restTemplate.getForObject("http://127.0.0.1:" + port + "/hello",
                String.class);
        Assertions.assertThat(requestResult).contains("Hello, spring");
    }

    /**
     * 测试 ThreadPoolTaskScheduler 和 ScheduledThreadPoolExecutor
     */
    @Test
    public void testTaskScheduler() {
        log.info("开始测试 TaskScheduler 线程池");
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = threadPoolTaskScheduler.getScheduledThreadPoolExecutor();
        // 主线程执行完后延迟5秒执行
        ScheduledFuture<?> scheduledFuture = scheduledThreadPoolExecutor.schedule(() -> {

            log.info("开始执行 TaskScheduler 线程池");
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 5, TimeUnit.SECONDS);
        try {
            scheduledFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        log.info("主线程执行完成....");
    }

    public static void main(String[] args) throws FileNotFoundException {

        // 算术类型图片验证码
        // ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        // captcha.setLen(2);  // 几位数运算，默认是两位
        // String arithmeticString = captcha.getArithmeticString();// 获取运算的公式：3+2=?
        // String text = captcha.text();// 获取运算的结果：5
        // System.out.println(arithmeticString + "  " + text);
        // FileOutputStream outputStream = new FileOutputStream("D:/captcha.png");
        // captcha.out(outputStream);  // 输出验证码

        // String a = "123";
        // String b = a.intern();
        // System.out.println(a == b);
        //
        // System.out.println(new StringJoiner(".").add("123").add("abc").toString());

        System.out.println(BigDecimal.valueOf(152365).divide(new BigDecimal("100")));

    }
}

