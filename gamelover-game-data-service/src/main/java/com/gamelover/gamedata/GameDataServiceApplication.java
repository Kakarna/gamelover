package com.gamelover.gamedata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.gamelover.gamedata.mapper")
public class GameDataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameDataServiceApplication.class, args);
    }
}
