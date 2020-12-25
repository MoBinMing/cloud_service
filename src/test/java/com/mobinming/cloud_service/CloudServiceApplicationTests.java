package com.mobinming.cloud_service;

import com.mobinming.cloud_service.service.BookService;
import com.mobinming.cloud_service.util.MyShiroRedisManager;
import org.crazycake.shiro.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.mockito.Mockito.verify;

@SpringBootTest
class CloudServiceApplicationTests {

    @Autowired
    RedisManager redisManager;

    @Test
    public void test1M(){
        MyShiroRedisManager manager=new MyShiroRedisManager(redisManager);
        //manager.getJedis().set("a","aaa");
        System.out.println(manager.getJedis().get("a"));;


//       RedisCache<String,String> cache= new RedisCache<>(redisManager, new StringSerializer(), new StringSerializer(), "employee:", 1, RedisCacheManager.DEFAULT_PRINCIPAL_ID_FIELD_NAME);
//        cache.put("a","aaa");
//        //Jedis a=redisManager.getJedisPool().getResource();
//        //a.set("a","aaa");
//        //String b= cache.getRedisCacheKey("a");
//        cache.setKeyPrefix("ppp");
        //System.out.println(b);
    }
}
