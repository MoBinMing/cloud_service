package com.mobinming.cloud_service.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobinming.cloud_service.entity.Book;
import com.mobinming.cloud_service.service.BookService;
import org.crazycake.shiro.RedisManager;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceImplTest {
    @Resource
    RedisManager redisManager;
    @Resource
    BookService bookService;
    @Test
    void getList() {
        Jedis jedis = null;
        List<Book> list=null;
        Gson gson=new Gson();
        try {
            jedis=redisManager.getJedisPool().getResource();//manager.getJedis().set("a","aaa");
            list=gson.fromJson(jedis.get("Books"),new TypeToken<List<Book>>() {}.getType());
        }catch (Exception e){
            list=bookService.list();
        }finally {
            if (list==null){
                list=bookService.list();
                try {
                    assert jedis != null;
                    jedis.set("Books",gson.toJson(list));
                    jedis.expire("Books", 10000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}