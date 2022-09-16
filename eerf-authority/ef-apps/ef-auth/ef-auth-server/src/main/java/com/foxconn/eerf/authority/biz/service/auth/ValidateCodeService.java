package com.foxconn.eerf.authority.biz.service.auth;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
/**
 * 验证码
 */
public interface ValidateCodeService {
    /**
     * 生成验证码
     */
    void create(String key, HttpServletResponse response) throws IOException;
}
