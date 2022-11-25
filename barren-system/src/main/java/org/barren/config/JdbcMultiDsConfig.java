package org.barren.config;

import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置 从数据库读取数据源
 *
 * @author cxs
 **/
@Primary
@Configuration
public class JdbcMultiDsConfig {

    @Value("${spring.datasource.dynamic.datasource.master.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String url;
    @Value("${spring.datasource.dynamic.datasource.master.username}")
    private String username;
    @Value("${spring.datasource.dynamic.datasource.master.password}")
    private String password;

    /**
     * <pre>
     * 自定义数据源
     * 可以通过实现AbstractDataSourceProvider类来自定义加载数据源来源的方式。
     * mybatis plus 默认是通过 YmlDynamicDataSourceProvider 实现了读取yml文件配置来初始化数据源的方式。
     * 配置jdbc读取数据源之后，yml+jdbc 2个方式共同加载数据源
     * </pre>
     *
     * @return
     */
    @Bean
    public DynamicDataSourceProvider jdbcDynamicDataSourceProvider() {
        return new AbstractJdbcDataSourceProvider(driverClassName, url, username, password) {
            @Override
            protected Map<String, DataSourceProperty> executeStmt(Statement statement) {
                Map<String, DataSourceProperty> dataSourcePropertiesMap = null;
                ResultSet rs = null;
                try {
                    dataSourcePropertiesMap = new HashMap<>();
                    // 查询数据源所在表
                    rs = statement.executeQuery("SELECT * FROM DYNAMIC_DATASOURCE_INSTANCE");
                    while (rs.next()) {
                        String name = rs.getString("name");
                        DataSourceProperty property = new DataSourceProperty();
                        property.setDriverClassName(rs.getString("driver"));
                        property.setUrl(rs.getString("url"));
                        property.setUsername(rs.getString("username"));
                        property.setPassword(rs.getString("password"));
                        dataSourcePropertiesMap.put(name, property);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return dataSourcePropertiesMap;
            }
        };
    }

}
