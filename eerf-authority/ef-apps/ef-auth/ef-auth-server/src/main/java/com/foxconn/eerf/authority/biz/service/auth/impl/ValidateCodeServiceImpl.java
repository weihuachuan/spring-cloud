package com.foxconn.eerf.authority.biz.service.auth.impl;

import com.foxconn.eerf.authority.biz.service.auth.ValidateCodeService;
import com.foxconn.eerf.common.constant.CacheKey;
import com.foxconn.eerf.exception.BizException;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Autowired
    private CacheChannel cache;

    @Override
    public void create(String key,
                       HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(key)) {
            throw BizException.validFail("验证码key不能为空");
        }

        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);

        Captcha captcha = new ArithmeticCaptcha(115, 42);
        captcha.setCharType(2);

        cache.set(CacheKey.CAPTCHA, key, StringUtils.lowerCase(captcha.text()));
        captcha.out(response.getOutputStream());
    }

    //校验验证码
    @Override
    public boolean check(String key, String value) {
        if (StringUtils.isBlank(value)) {
            throw BizException.validFail("请输入验证码");
        }
        //根据key从缓存中获取验证码
        CacheObject cacheObject = cache.get(CacheKey.CAPTCHA, key);
        if (cacheObject.getValue() == null) {
            throw BizException.validFail("验证码已过期");
        }
        //比对验证码
        if (!StringUtils.equalsIgnoreCase(value,
                String.valueOf(cacheObject.getValue()))) {
            throw BizException.validFail("验证码不正确");
        }
        //验证通过，立即从缓存中删除验证码
        cache.evict(CacheKey.CAPTCHA, key);
        return true;
    }
}
