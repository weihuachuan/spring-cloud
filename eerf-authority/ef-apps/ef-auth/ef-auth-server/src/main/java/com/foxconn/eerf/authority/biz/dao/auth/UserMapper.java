package com.foxconn.eerf.authority.biz.dao.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foxconn.eerf.authority.entity.auth.User;
import org.springframework.stereotype.Repository;

/**
 * Mapper 接口
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
}
