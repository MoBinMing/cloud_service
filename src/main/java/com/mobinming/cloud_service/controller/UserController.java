package com.mobinming.cloud_service.controller;


import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mobinming.cloud_service.entity.User;
import com.mobinming.cloud_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mbm
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/cloud_service/user/")
public class UserController {
    @Autowired
    protected UserService userService;

    @PostMapping({"login"})
    @ResponseBody
    public Object login(@RequestBody String json) {
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        JSONObject object = new JSONObject();
        JSONObject jsonObject1 = JSONObject.parseObject(json);
        String username = jsonObject1.getString("username");
        String password = jsonObject1.getString("password");
        User one = this.userService.getOne(
                new QueryWrapper<User>().lambda().eq(User::getUserName, false), false);
        if (one != null &&
                one.getUserName().equals(username) && one.getPassword().equals(password)) {
            String token = getToken(one);
            object.put("token", token);
            object.put("message", "登录成功");
            request.setAttribute("token",token);
            return object;
        }
        object.put("token", null);
        object.put("message", "账号密码错误");
        return object;
    }

    @ResponseBody
    @PostMapping({"register"})
    public String register(@RequestBody JSONObject jsonObject) {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        if (this.userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUserName, username), false) == null &&
                this.userService.save(new User(username, password))) {
            return "注册成功";
        }

        return "注册失败";
    }

    public String getToken(User user) {
        String token = "";
        token = JWT.create().withAudience(String.valueOf(user.getId()))
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}

