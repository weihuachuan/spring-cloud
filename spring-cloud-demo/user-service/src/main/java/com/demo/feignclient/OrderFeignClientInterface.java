package com.demo.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "orderservice",fallback = OrderFeignClientFallback.class)
public interface OrderFeignClientInterface {

    //调用根据id获取用户的接口
    @GetMapping("/getGoods")
    String getGoods();
}
