package com.demo.mapper;

import com.demo.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface AccountMapper {

    Account getUser(String id);

    void updata(Account account);


}
