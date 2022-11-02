
package com.demo.controller;

import com.demo.feignclient.OrderFeignClientInterface;
import com.demo.service.AccountService;
import com.demo.service.AccountServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;


@RestController
//  动态更新配置
@RefreshScope
public class UserController {

    @Resource
    private OrderFeignClientInterface orderFeignClientInterface;

    @Autowired
    private AccountService accountService;

    // 通过从nacos配置中心获取
    @Value("${user}")
    private String user;


    @GetMapping("/buy")
    public String buy() {
        //  不使用nacos服务注册和发现
        //  String user = restTemplate.getForObject("http://localhost:8093/getGoods/", String.class);
        // 使用nacos中ribbon通过微服务名代替ip加端口号  这里访问order-service的微服务
        String goods = orderFeignClientInterface.getGoods();
        System.out.println("购买商品");
        return "用户：" + user + "买了一个：" + goods;
    }

    @GetMapping("/getUser")
    public String getUser() {
        System.out.println("获取用户名");
        return user;
    }

    @GetMapping("/decrease")
    public String increase(@RequestParam("userId") String userId, @RequestParam("money") BigDecimal money) {
        accountService.decrease(userId, money);
        return "ok";
    }
}