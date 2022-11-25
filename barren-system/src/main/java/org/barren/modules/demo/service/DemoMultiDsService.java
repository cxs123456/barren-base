package org.barren.modules.demo.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
    public void switchDs(){
        List<Map<String, Object>> maps1 = jdbcTemplate.queryForList("select database()");
        System.out.println(Thread.currentThread().getName() + "，当前使用的数据库： " + maps1);
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList("select * from demo02 where id >10");
        log.info("DataSource dataList = {}", dataList);
    }
}
