package com.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    private Long id;

    private String userId;

    private String product;

    private BigDecimal money;

    private Integer count;

}