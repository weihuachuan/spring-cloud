package com.huachuan.config;

/*
 *日志配置类
 */

import com.huachuan.service.LogService;
import com.itheima.pinda.log.event.SysLogListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogAutoConfiguration {
    //自动配置日志监听器组件
    @Bean
    @ConditionalOnMissingBean
    public SysLogListener sysLogListener(LogService logService){
        return new SysLogListener(optLogDTO -> logService.saveLog(optLogDTO));
    }
}
