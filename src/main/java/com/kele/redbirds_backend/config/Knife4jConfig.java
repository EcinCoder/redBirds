package com.kele.redbirds_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author kele
 */
@Configuration
@Profile("prod") //线上环境时，此配不生效
public class Knife4jConfig {


    @Bean
    public OpenAPI springShopOpenApi() {
        return new OpenAPI()
                // 接口文档标题
                .info(new Info().title("红鸟伙伴匹配系统")
                        // 接口文档简介
                        .description("红鸟伙伴匹配系统的测试接口文档")
                        // 接口文档版本
                        .version("1.0版本")
                        // 开发者联系方式
                        .contact(new Contact().name("可乐")
                                .email("xxxx@qq.com")));

    }

}