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
    @Resource
    private BookService service;
    @Value("${shiro-redis.redis-manager.password}")
    private String password;
    @Test
    void contextLoads() {
    }
    @Autowired
    RedisManager redisManager;
//    @Resource
//    RedisCacheManager redisCacheManager;
    @Test
    void test1M(){
        service.getList();
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
//    @Test
//    void testM(){
//        System.out.println(password);
//        MyRedisManagerUtil redisManager=new MyRedisManagerUtil();
//        Jedis jedis = redisManager.getJedisPool().getResource();
//        List<Book> list;
//        Gson gson=new Gson();
//        try {
//            list=gson.fromJson(redisManager.jedis.get("Books"),new TypeToken<List<Book>>() {}.getType());
//            if (list==null){
//                list=service.getList();
//                redisManager.jedis.set("Books",gson.toJson(list));
//                redisManager.jedis.expire("Books", 10000);
//            }
//        }catch (Exception e){
//            list=service.getList();
//            redisManager.jedis.set("Books",gson.toJson(list));
//            redisManager.jedis.expire("Books", 10000);
//        }
//    }
//
//    @Test
//    void testM1(){
//        System.out.println(password);
//        RedisManager redisManager=new RedisManager();
//        redisManager.setPassword(password);
//        Jedis jedis = redisManager.getJedisPool().getResource();
//        List<Book> list;
//        Gson gson=new Gson();
//        try {
//            list=gson.fromJson(jedis.get("Books"),new TypeToken<List<Book>>() {}.getType());
//            if (list==null){
//                list=service.getList();
//                jedis.set("Books",gson.toJson(list));
//                jedis.expire("Books", 10000);
//            }
//        }catch (Exception e){
//            list=service.getList();
//            jedis.set("Books",gson.toJson(list));
//            jedis.expire("Books", 10000);
//        }
//    }
//    @Resource
//    RedisSessionDAO redisSessionDAO;
//
//    @Resource
//    RedisCacheManager redisCacheManager;
//    @Resource
//    private MyRedisProperties ds;
//    @Test
//    void testRedis(){
//        RedisManager r = ds.redisManager();
//
//        redisSessionDAO.setRedisManager(new As());
//
//        MyShiroRedisManager redisManager=new MyShiroRedisManager();
//
//        Jedis jedis = redisManager.getJedis();
//        List<Book> list;
//        Gson gson=new Gson();
//        try {
//            list=gson.fromJson(jedis.get("Books"),new TypeToken<List<Book>>() {}.getType());
//            if (list==null){
//                list=service.getList();
//                jedis.set("Books",gson.toJson(list));
//                jedis.expire("Books", 10000);
//            }
//        }catch (Exception e){
//            list=service.getList();
//            jedis.set("Books",gson.toJson(list));
//            jedis.expire("Books", 10000);
//        }
//    }
}
