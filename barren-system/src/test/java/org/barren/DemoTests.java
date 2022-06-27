package org.barren;

import com.wf.captcha.ArithmeticCaptcha;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Spring-Boot整合JUnit5的单元测试
 *
 * @author cxs
 * @date 2021/12/3 12:19
 **/
//@WebMvcTest // 分层测试方法
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testHello() {
        String requestResult = this.restTemplate.getForObject("http://127.0.0.1:" + port + "/hello",
                String.class);
        Assertions.assertThat(requestResult).contains("Hello, spring");
    }


    public static void main(String[] args) throws FileNotFoundException {

        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setLen(2);  // 几位数运算，默认是两位
        String arithmeticString = captcha.getArithmeticString();// 获取运算的公式：3+2=?
        String text = captcha.text();// 获取运算的结果：5
        System.out.println(arithmeticString + "  " + text);
        FileOutputStream outputStream = new FileOutputStream("D:/captcha.png");
        captcha.out(outputStream);  // 输出验证码

    }
}

