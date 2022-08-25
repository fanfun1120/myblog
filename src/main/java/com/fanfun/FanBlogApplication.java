package com.fanfun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fanfun.mapper")
public class FanBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(FanBlogApplication.class, args);
    }

}
