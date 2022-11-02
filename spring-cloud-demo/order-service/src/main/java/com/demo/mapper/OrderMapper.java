package com.demo.mapper;


import com.demo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderMapper {
    void create(Order order);
}
