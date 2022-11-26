package org.barren.modules.demo.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cxs
 */
@Slf4j
@Service
@DS("test")
public class DemoMultiDsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @DS("slave_1")
    public void switchDs() {
        List<Map<String, Object>> maps1 = jdbcTemplate.queryForList("select database()");
        System.out.println(Thread.currentThread().getName() + "，当前使用的数据库： " + maps1);
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList("select * from demo02 where id >10");
        log.info("DataSource dataList = {}", dataList);
    }

    @Async
    public void asyncDemo() {
        log.info("async start exec ........");
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        log.info("async end exec ........");
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduledDemo() {
        log.info("定时任务开始执行时间：{}", LocalDateTime.now());
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss.SSS");
        log.info("定时任务完成执行时间：{}", sdf.format(new Date()));
    }
}
