package org.barren.tests;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.barren.modules.demo.service.DemoMultiDsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 测试 baomidou 多数据源
 *
 * @author cxs
 */
@Slf4j
@Service
@SpringBootTest
public class MultiDsServiceTest {

    @Resource
    private DataSource dataSource;
    @Resource
    private DefaultDataSourceCreator dataSourceCreator;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DemoMultiDsService demoMultiDsService;

    @BeforeEach
    public void setup() {

    }

    /**
     * 查询 当前加载的数据源列表
     */
    @Test
    public void listDs() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        Map<String, DataSource> dataSources = ds.getDataSources();
        log.info("DataSource list = {}", dataSources.keySet());
    }

    @Test
    public void addDs() {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai");
        dataSourceProperty.setUsername("root");
        dataSourceProperty.setPassword("123456");
        dataSourceProperty.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceProperty.setPoolName("test99999");

        // 1、内存中添加数据源
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        ds.addDataSource(dataSourceProperty.getPoolName(), dataSource);
        // 2、保存到数据库中
        int count = jdbcTemplate.update("INSERT INTO dynamic_datasource_instance (type, name, username, password, url, driver) VALUES(?,?,?,?,?,?)",
                "mysql",
                dataSourceProperty.getPoolName(),
                dataSourceProperty.getUsername(),
                dataSourceProperty.getPassword(),
                dataSourceProperty.getUrl(),
                dataSourceProperty.getDriverClassName());
        System.out.println(count);

    }

    @Test
    public void deleteDs() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        ds.removeDataSource("test99999");

        int count = jdbcTemplate.update("DELETE FROM dynamic_datasource_instance WHERE name = ?", "test99999");
        System.out.println(count);
    }


    /**
     * <pre>
     * 注解方式切换数据源：
     * 1、名称，比如： slave_1
     * 2、请求头，如：#header.datasource
     * 3、spel 表达式
     * </pre>
     */
    @Test
    @DS("test")
    public void switchDs() {
        demoMultiDsService.switchDs();
    }


    /**
     * 多数据源切换示例： 代码中 手动切换多个数据源
     * 手动切换数据源 A数据源-->B数据源-->C数据源-->A数据源
     */
    @Test
    public void testMultiDsExec() {
        // 数据源0  默认数据源 db
        List<Map<String, Object>> maps0 = jdbcTemplate.queryForList("select database()");
        System.out.println(Thread.currentThread().getName() + " 000当前使用的数据库： " + maps0);

        try {
            // 数据源1 test
            DynamicDataSourceContextHolder.push("test");
            List<Map<String, Object>> maps1 = jdbcTemplate.queryForList("select database()");
            System.out.println(Thread.currentThread().getName() + " 111当前使用的数据库： " + maps1);
            Thread.sleep(3000);

            DynamicDataSourceContextHolder.poll();


            // 数据源2 pinyougoudb
            DynamicDataSourceContextHolder.push("pinyougoudb");
            List<Map<String, Object>> maps2 = jdbcTemplate.queryForList("select database()");
            System.out.println(Thread.currentThread().getName() + " 222当前使用的数据库： " + maps2);
            Thread.sleep(2000);
            DynamicDataSourceContextHolder.poll();


            // 数据源3 默认数据源 db
            List<Map<String, Object>> maps3 = jdbcTemplate.queryForList("select database()");
            System.out.println(Thread.currentThread().getName() + " 333当前使用的数据库： " + maps3);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
    }

}
