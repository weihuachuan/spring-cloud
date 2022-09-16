package com.foxconn.eerf.authority.config;

import com.foxconn.eerf.authority.biz.service.common.OptLogService;
import com.foxconn.eerf.log.entity.OptLogDTO;
import com.foxconn.eerf.log.event.SysLogListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import java.util.function.Consumer;
/**
 * 日志自动配置
 */
@EnableAsync
@Configuration
public class SysLogConfiguration {
    //日志记录监听器
    @Bean
    public SysLogListener sysLogListener(OptLogService optLogService) {
        Consumer<OptLogDTO> consumer = (optLog) -> optLogService.save(optLog);
        return new SysLogListener(consumer);
    }
}
