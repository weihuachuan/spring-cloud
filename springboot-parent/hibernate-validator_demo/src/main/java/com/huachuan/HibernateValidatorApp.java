package com.huachuan;

import com.huachuan.config.EnableFormValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFormValidator
public class HibernateValidatorApp {
    public static void main(String[] args) {
        SpringApplication.run(HibernateValidatorApp.class,args);
    }
}
