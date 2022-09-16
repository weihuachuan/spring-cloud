package com.foxconn.eerf.authority.biz.service.auth;

import com.baomidou.mybatisplus.extension.service.IService;
import com.foxconn.eerf.authority.dto.auth.ResourceQueryDTO;
import com.foxconn.eerf.authority.entity.auth.Resource;

import java.util.List;

/**
 * 业务接口
 */
public interface ResourceService extends IService<Resource> {
    /**
     * 查询 用户拥有的资源权限
     */
    List<Resource> findVisibleResource(ResourceQueryDTO resource);
}
