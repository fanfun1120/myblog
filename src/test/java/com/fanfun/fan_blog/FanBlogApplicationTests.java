package com.fanfun.fan_blog;

import com.fanfun.test.SysProductOutlay;
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

    @Test
    public void testList() {
        //新建list 添加顺序与分区号一致
        List<SysProductOutlay> codeList = new ArrayList<>();
        List<SysProductOutlay> sysProductOutlayList = new ArrayList<>();

//        List<SysProductOutlay> list = new ArrayList<>();
        //遍历分区集合，确保添加价格时 与分区号出现的顺序一致
        for (SysProductOutlay productOutlay : codeList) {
            for (SysProductOutlay product : sysProductOutlayList) {
                //当分区号相同时添加价格到集合中
                if (productOutlay.getAreaCode().equals(product.getAreaCode())) {
                    list.add(product);
                }
            }
        }

        Map<Integer, SysProductOutlay> collect = codeList.stream().collect(Collectors.toMap(productOutlay -> productOutlay.getAreaCode(),
                productOutlay -> productOutlay
        ));
        List<SysProductOutlay> list = sysProductOutlayList.stream().map(product -> {
                    return codeList.get(product.getAreaCode());
                }
        ).collect(Collectors.toList());


    }

}
