package com.mobinming.cloud_service.controller.api;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mobinming.cloud_service.common.dto.LoginDto;
import com.mobinming.cloud_service.common.dto.RegisterDto;
import com.mobinming.cloud_service.common.lang.Result;
import com.mobinming.cloud_service.entity.User;
import com.mobinming.cloud_service.service.UserService;
import com.mobinming.cloud_service.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/")
public class UserApiController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    protected UserService userService;

    @CrossOrigin
    @PostMapping("login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        User user = userService.getOne(new QueryWrapper<User>().eq("user_name", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在");
        if(!user.getPassword().equals(loginDto.getPassword())) {
            return Result.fail("账号或密码错误！");
        }
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        // 用户可以另一个接口
        return Result.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUserName())
                .put("token",jwt)
                .map()
        );
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

    @CrossOrigin
    //用@RequestBody取值ajax用json字符串
    //表单去除@RequestBody
    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterDto registerDto) {
        User userP = userService.getOne(new QueryWrapper<User>().eq("phone", registerDto.getPhone()));
        User userU = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserName, registerDto.getUserName()),false);
        if (userP!=null){
            Result.succ("注册失败,手机号:"+registerDto.getPhone()+"被占用");
        }else if (userU!=null){
            Result.succ("注册失败，用户名:"+registerDto.getUserName()+"被占用");
        }else{
            User user = new User();
            BeanUtil.copyProperties(registerDto, user);
            if (userService.save(user)){
                return Result.succ("注册成功");
            }
        }
        return Result.succ("注册失败,插入数据量失败");
    }

    @CrossOrigin
    @GetMapping("/usernameIsAvailable")
    public Result usernameIsAvailable(@RequestParam("username") String username) {
        if (username!=null){
            User one = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserName, username),false);
            if (one==null){
                return Result.succ("Available");
            }
            return Result.succ("unavailable");
        }
        return Result.succ("参数username非法");
    }

    @CrossOrigin
    @GetMapping("/phoneIsAvailable")
    public Result phoneIsAvailable(@RequestParam("phone") String phone) {
        if (phone!=null){
            User one = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone),false);
            if (one==null){
                return Result.succ("Available");
            }
            return Result.succ("unavailable");
        }
        return Result.succ("参数phone非法");
    }
}

