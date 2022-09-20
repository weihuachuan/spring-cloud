package com.foxconn.eerf.authority.biz.service.common;

import com.baomidou.mybatisplus.extension.service.IService;
import com.foxconn.eerf.authority.entity.common.OptLog;
import com.foxconn.eerf.log.entity.OptLogDTO;

/**
 * 业务接口
 * 操作日志
 */
public interface OptLogService extends IService<OptLog> {
    /**
     * 保存日志
     */
    boolean save(OptLogDTO entity);
}
