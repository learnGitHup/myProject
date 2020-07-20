package com.example.common.mongoconf;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author Si LingChao
 * @date 2020/7/2 15:43
 */
@Configuration
public class MongoConf {

    @Primary
    @Bean(name = "rateProperties")
    @ConfigurationProperties("spring.data.mongodb.rate")
    public MongoProperties rateMongoProperties(){
       return new MongoProperties();

    }
}
