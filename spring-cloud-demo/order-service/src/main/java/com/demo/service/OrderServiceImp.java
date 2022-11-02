package com.demo.service;

import com.demo.entity.Order;
import com.demo.feignclient.UserFeignClient;
import com.demo.mapper.OrderMapper;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class OrderServiceImp implements OrderService{

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImp.class);
    @Resource
    private UserFeignClient userFeignClient;

    @Autowired
    private OrderMapper orderMapper;


    @Override
    @GlobalTransactional
//    @Transactional
    public void create(String userId, String product, Integer count) {
        log.info("Seata全局事务id=================>{}", RootContext.getXID());

        BigDecimal orderMoney = new BigDecimal(count).multiply(new BigDecimal(5));

        Order order = new Order();
        order.setUserId(userId);
        order.setProduct(product);
        order.setCount(count);
        order.setMoney(orderMoney);

        orderMapper.create(order);
        userFeignClient.decrease(userId, orderMoney);

    }
}
