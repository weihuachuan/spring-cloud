package com.foxconn.eerf.authority.biz.dao.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foxconn.eerf.authority.entity.auth.UserRole;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * 角色分配
 * 账号角色绑定
 * </p>
 *
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
