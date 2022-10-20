package com.foxconn.eerf.config;

import com.foxconn.eerf.database.datasource.BaseMybatisConfiguration;
import com.foxconn.eerf.database.properties.DatabaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis相关配置
 */
@Configuration
@Slf4j
public class GoodsMybatisAutoConfiguration extends BaseMybatisConfiguration {
    public GoodsMybatisAutoConfiguration(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }
}
