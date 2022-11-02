package com.demo.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface AccountService {

    void decrease(String userId, BigDecimal money);
}
