package com.demo.feignclient;


import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserFeignClientFallback implements UserFeignClient {

    @Override
    public String getUser() {
        return "查询不到用户";
    }

    @Override
    public void decrease(String  userId, BigDecimal money) {

    }
}
