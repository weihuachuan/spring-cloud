package com.foxconn.eerf.authority.controller.auth;

import com.foxconn.eerf.authority.biz.service.auth.impl.AuthManager;
import com.foxconn.eerf.authority.dto.auth.LoginDTO;
import com.foxconn.eerf.authority.dto.auth.LoginParamDTO;
import com.foxconn.eerf.base.BaseController;
import com.foxconn.eerf.base.R;
import com.foxconn.eerf.exception.BizException;
import com.foxconn.eerf.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.foxconn.eerf.authority.biz.service.auth.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;




/**
 * 登录
 */
@RestController
@RequestMapping("/anno")
@Api(value = "UserAuthController", tags = "登录")
@Slf4j

public class LoginController extends BaseController {
    @Autowired
    private AuthManager authManager;//认证管理器对象

    @Autowired
    private ValidateCodeService validateCodeService;

    @ApiOperation(value = "验证码", notes = "验证码")
    @GetMapping(value = "/captcha", produces = "image/png")
    public void captcha(@RequestParam(value = "key") String key,
                        HttpServletResponse response) throws IOException {
        this.validateCodeService.create(key, response);
    }
    /**
     * 登录认证
     */
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping(value = "/login")
    public R<LoginDTO> login(@Validated @RequestBody LoginParamDTO login)
            throws BizException {
        log.info("account={}", login.getAccount());
        if (this.validateCodeService.check(login.getKey(), login.getCode())) {
            return this.authManager.login(login.getAccount(), login.getPassword());
        }
        return this.success(null);
    }
}

