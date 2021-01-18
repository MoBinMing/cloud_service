package com.mobinming.cloud_service.service.impl;

import cn.hutool.core.lang.Assert;
import com.mobinming.cloud_service.service.BookService;
import org.crazycake.shiro.RedisManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

//@RunWith(SpringRunner.class)
class BookServiceImplTest {
    @Resource
    RedisManager redisManager;
    @Resource
    private BookService bookService;
    @Test
    void getList() {
        Assert.notNull(bookService.getList(),"eeee");
    }
}