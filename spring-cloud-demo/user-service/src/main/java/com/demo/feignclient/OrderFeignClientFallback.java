package com.demo.feignclient;


import org.springframework.stereotype.Component;

@Component
public class OrderFeignClientFallback implements OrderFeignClientInterface {

    @Override
    public String getGoods() {
        return "获取不到商品";
    }
}
