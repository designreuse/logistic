package com.frame.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig{

    @Autowired
    private Environment env;

    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @Primary
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create()
                .url(env.getProperty("spring.datasource.url"))
                .driverClassName(env.getProperty("spring.datasource.driverClassName"))
                .username(env.getProperty("spring.datasource.username"))
                .password(env.getProperty("spring.datasource.password"))
                .type(com.alibaba.druid.pool.DruidDataSource.class)
                .build();
    }

    @Bean(name = "camundaBpmDataSource")
    @Qualifier("camundaBpmDataSource")
    public DataSource camundaBpmDataSource() {
        return DataSourceBuilder.create()
                .url(env.getProperty("camunda.bpm.datasource.url"))
                .driverClassName(env.getProperty("camunda.bpm.datasource.driverClassName"))
                .username(env.getProperty("camunda.bpm.datasource.username"))
                .password(env.getProperty("camunda.bpm.datasource.password"))
                .type(com.alibaba.druid.pool.DruidDataSource.class)
                .build();
    }

    @Bean(name="transactionManager")
    public PlatformTransactionManager transactionManager(DataSource primaryDataSource){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(primaryDataSource);
        return transactionManager;
    }
}
