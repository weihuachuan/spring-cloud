package com.demo.controller;


import com.demo.feignclient.UserFeignClient;
import com.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RefreshScope
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Resource
    private UserFeignClient userFeignClient;


    // 通过从nacos配置中心获取
    @Value("${goods}")
    private String goods;

    @GetMapping("/order")
    public String order() {

        String user = userFeignClient.getUser();
        System.out.println("打印订单");
        return "订单：" + goods + "===== 购买人：" + user;
    }

    @GetMapping("/getGoods")
    public String getGoods() {
        System.out.println("获取商品");
        return goods;
    }

    @GetMapping("/create")
    public String create(@RequestParam("userId") String userId, @RequestParam("commodityCode") String commodityCode, @RequestParam("count") Integer count) {
        orderService.create(userId, commodityCode, count);
        return "购买成功";
    }
}
