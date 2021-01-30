package com.mobinming.cloud_service.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.mobinming.cloud_service.common.dto.UserDao;
import com.mobinming.cloud_service.service.BookService;
import lombok.Data;
import org.crazycake.shiro.RedisManager;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

//@RunWith(SpringRunner.class)
class BookServiceImplTest {

    @Data
    private class userDto{
        private Date registerTime;
    }
    @Test
    public void Atest() {
    }
    @Resource
    RedisManager redisManager;
    @Resource
    private BookService bookService;
    @Test
    void getList() {
            UserDao userDao = new UserDao();
            userDao.setRegisterTime(new Date());
            String json = JSONUtil.toJsonStr(userDao);
            UserDao userDao1 = JSONUtil.toBean(json, UserDao.class);

        Assert.notNull(bookService.getList(),"eeee");
    }
}