package com.demo.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "userservice")
public interface UserFeignClient {

    //调用根据id获取用户的接口
    @GetMapping("/getUser")
    String getUser();

    @GetMapping("/decrease")
    void decrease( @RequestParam("userId") String userId, @RequestParam("money") BigDecimal money);
}
