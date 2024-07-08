//package com.kele.redbirds_backend.config;/*
// * @author kele
// * @version 1.0
// *
// */
//
//import lombok.Data;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConfigurationProperties(prefix = "spring.redis") // 直接从配置文件中读取
//@Data
//public class RedissonConfig {
//
//    private String host;
//
//    private String port;
//
//    @Bean
//    public RedissonClient redissonClient() {
//        // 1. 创建配置
//        Config config = new Config();
//        String redisAddress = String.format("redis://%s:%s",host,port);
//        config.useSingleServer().setAddress(redisAddress).setDatabase(3);
//
//        // 2. 创建实例
//        return Redisson.create(config);
//    }
//}
