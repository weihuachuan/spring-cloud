package com.foxconn.eerf.auth.client;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.foxconn.eerf.auth.client.configuration.AuthClientConfiguration;

import org.springframework.context.annotation.Import;

/**
 * 启用授权client
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({AuthClientConfiguration.class})
@Documented
@Inherited
public @interface EnableAuthClient {
}
