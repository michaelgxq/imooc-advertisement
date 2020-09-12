package com.imooc.advertisement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ClassName: EurekaApplication
 * @Description: TODO
 * @author: yourName
 * @date: 2020年04月19日 9:13
 */
@SpringBootApplication
@EnableEurekaServer //此注解用于启动Eureka服务
@MapperScan("com.imooc.advertisement.dao")
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
