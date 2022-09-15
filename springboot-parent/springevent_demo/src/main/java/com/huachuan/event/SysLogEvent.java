package com.huachuan.event;

import com.huachuan.dto.OptLogDTO;
import org.springframework.context.ApplicationEvent;

public class SysLogEvent extends ApplicationEvent {
    public SysLogEvent(OptLogDTO optLogDTO) {
        super(optLogDTO);
    }
}
