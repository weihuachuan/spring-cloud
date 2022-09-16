package com.foxconn.eerf.auth.server.configuration;


import com.foxconn.eerf.auth.server.properties.AuthServerProperties;
import com.foxconn.eerf.auth.server.utils.JwtTokenServerUtils;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 认证服务端配置
 *
 */
@EnableConfigurationProperties(value = {
        AuthServerProperties.class,
})
public class AuthServerConfiguration {
    @Bean
    public JwtTokenServerUtils getJwtTokenServerUtils(AuthServerProperties authServerProperties) {
        return new JwtTokenServerUtils(authServerProperties);
    }
}
