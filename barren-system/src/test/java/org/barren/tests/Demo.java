package org.barren.tests;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.barren.modules.system.entity.SysUser;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

/**
 * 单纯测试
 *
 * @author cxs
 **/
@Slf4j
public class Demo {

    @Test
    public void test() {
        String a = "/" + null;
        System.out.println(a);

        log.error(" getInstitution error. institutionId={}", 1111, new Exception("1111"));
    }

    @Test
    public void json() {

        String json = "{\n" +
                "    \"phone\": \"1320335756\",\n" +
                "    \"user_name\": \"aaa\",\n" +
                "    \"scope\": [\n" +
                "        \"all\"\n" +
                "    ],\n" +
                "    \"roles\": [\n" +
                "        \"admin\",\n" +
                "        \"user\"\n" +
                "    ],\n" +
                "    \"nickname\": \"小鸟\",\n" +
                "    \"name\": null,\n" +
                "    \"id\": \"8789\",\n" +
                "    \"exp\": 1656109724,\n" +
                "    \"ok\": true,\n" +
                "    \"authorities\": [\n" +
                "        \"admin\",\n" +
                "        \"user\"\n" +
                "    ],\n" +
                "    \"jti\": \"05dccd13-d145-4d11-85e2-977c2f1c10e8\",\n" +
                "    \"client_id\": \"web\"\n" +
                "}";


        SysUser user = JSON.parseObject(json, SysUser.class);

        System.out.println(user);
    }


    @Test
    public void testStream(){
        // Streams 静态方法：of()、iterate()、generate()
        Stream<Integer> stream1 = Stream.of(1, 3, 4, 6, 7, 8);
        stream1.forEach(System.out::println);

        Stream<Integer> stream2 = Stream.iterate(1, x -> x + 2).limit(4);
        stream2.forEach(System.out::println);

        Stream<Double> stream3 = Stream.generate(Math::random).limit(5);
        stream3.forEach(System.out::println);

        LinkedBlockingQueue<Object> linkedBlockingQueue = new LinkedBlockingQueue<>(1024);
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue<>(1024);




    }

    public static void main(String[] args)  {

        String name = Demo.class.getName();
        String simpleName = Demo.class.getSimpleName();
        String typeName = Demo.class.getTypeName();
        System.out.println(name);
        System.out.println(simpleName);
        System.out.println(typeName);

        // int i = minMoves(new int[]{1, 2, 3});
        // System.out.println(i);
    }


    public static int minMoves(int[] nums) {
        int min = Arrays.stream(nums).min().getAsInt();
        int sum = Arrays.stream(nums).sum();
        return sum - min * nums.length ;
    }


}
