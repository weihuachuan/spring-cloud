package com.demo.service;


import org.springframework.stereotype.Service;


public interface OrderService {
    void create(String  userId, String product, Integer count);
}
