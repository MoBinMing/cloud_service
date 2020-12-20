package com.mobinming.cloud_service.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobinming.cloud_service.entity.Book;
import com.mobinming.cloud_service.mapper.BookMapper;
import com.mobinming.cloud_service.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobinming.cloud_service.util.MyShiroRedisManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mbm
 * @since 2020-11-15
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Resource
    RedisManager redisManager;

    @Override
    public List<Book> getList() {
        Jedis jedis = null;
        List<Book> list=null;
        Gson gson=new Gson();
        try {
            MyShiroRedisManager manager=new MyShiroRedisManager(redisManager);
            jedis=manager.getJedis();//manager.getJedis().set("a","aaa");
            list=gson.fromJson(jedis.get("Books"),new TypeToken<List<Book>>() {}.getType());
        }catch (Exception e){
            list=list();
        }finally {
            if (list==null){
                list=list();
                try {
                    assert jedis != null;
                    jedis.set("Books",gson.toJson(list));
                    jedis.expire("Books", 10000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
         return list;
    }

}
