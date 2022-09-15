package com.foxconn.eerf.auth.client.configuration;


import com.foxconn.eerf.auth.client.properties.AuthClientProperties;
import com.foxconn.eerf.auth.client.utils.JwtTokenClientUtils;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 客户端认证配置
 *
 */
@EnableConfigurationProperties(value = {
        AuthClientProperties.class,
})
public class AuthClientConfiguration {
    @Bean
    public JwtTokenClientUtils getJwtTokenClientUtils(AuthClientProperties authClientProperties) {
        return new JwtTokenClientUtils(authClientProperties);
    }

}
