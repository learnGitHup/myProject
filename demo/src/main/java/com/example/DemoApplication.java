package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@MapperScan("com.example.mapper")
//@EnableTransactionManagement
@SpringBootApplication
@PropertySource(value = {"classpath:ftpPoolConfig.properties"}, encoding = "utf-8")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
