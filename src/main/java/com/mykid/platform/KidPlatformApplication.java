package com.mykid.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author MrBird
 */
@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@MapperScan("com.mykid.platform.mapper")
public class KidPlatformApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(KidPlatformApplication.class)
                .run(args);
    }

}
