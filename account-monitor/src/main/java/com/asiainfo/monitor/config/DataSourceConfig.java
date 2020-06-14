package com.asiainfo.monitor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ch
 * @Date: 2018/6/27 11:06
 * @Description:
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "db")
    @ConfigurationProperties(prefix = "spring.datasource.db")
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db2")
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource dataSource2() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db3")
    @ConfigurationProperties(prefix = "spring.datasource.db3")
    public DataSource dataSource3() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db4")
    @ConfigurationProperties(prefix = "spring.datasource.db4")
    public DataSource dataSource4() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db5")
    @ConfigurationProperties(prefix = "spring.datasource.db5")
    public DataSource dataSource5() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 动态数据源: 通过AOP在不同数据源之间动态切换
     *
     * @return
     */
    @Primary
    @Bean(name = "dynamicDS1")
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(dataSource1());

        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap(5);
        dsMap.put("db", dataSource1());
        dsMap.put("db2", dataSource2());
        dsMap.put("db3", dataSource3());
        dsMap.put("db4", dataSource4());
        dsMap.put("db5", dataSource5());
        dynamicDataSource.setTargetDataSources(dsMap);
        return dynamicDataSource;
    }
    /**
     * 配置@Transactional注解事物
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
