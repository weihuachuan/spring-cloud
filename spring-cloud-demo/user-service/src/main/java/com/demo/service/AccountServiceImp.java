package com.demo.service;

import com.demo.entity.Account;
import com.demo.mapper.AccountMapper;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountServiceImp implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImp.class);

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void decrease(String  userId, BigDecimal money) {
        log.info("Seata全局事务id=================>{}", RootContext.getXID());
        Account account = accountMapper.getUser(userId);
        account.setMoney(account.getMoney().subtract(money));
        accountMapper.updata(account);
    }
}
