package com.fanfun.fan_blog;

import com.fanfun.util.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class FanBlogApplicationTests {

    @Autowired
    private RedisCache redisCache;

    @Test
    void contextLoads() {
        System.out.println("sadf");
    }

    @Test
    public void redisTest() {
        redisCache.setCacheObject("testredis", "123");
        String testredis = (String) redisCache.getCacheObject("testredis");
        System.out.println(testredis);
    }

}
