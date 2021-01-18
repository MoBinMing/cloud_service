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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mbm
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/api")
@Api(tags = "账户相关模块")
public class UserApiController {
    @Resource
    protected UserService userService;

    @CrossOrigin
    @ApiOperation("用户登录")
    @PostMapping(value = "/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        return userService.login(loginDto,response);
    }

    @RequiresAuthentication
    @ApiOperation(value="用户推出登录", notes="这是用户推出登录接口")
    @GetMapping("/logout")
    public Result logout() {
        return userService.logout();
    }

    @CrossOrigin
    @ApiOperation(value="用户注册", notes="这是用户注册接口")
    //用@RequestBody取值ajax用json字符串
    //表单去除@RequestBody
    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterDto registerDto) {
        return userService.register(registerDto);
    }

    @CrossOrigin
    @ApiOperation(value="用户名是否可用", notes="检查用户名是否被占用")
    @GetMapping("/usernameIsAvailable")
    public Result usernameIsAvailable(@RequestParam("aliasName") String aliasName) {
        return userService.usernameIsAvailable(aliasName);
    }

    @CrossOrigin
    @ApiOperation(value="手机号是否可用", notes="检查手机号是否被占用")
    @GetMapping("/phoneIsAvailable")
    public Result phoneIsAvailable(@RequestParam("phone") String phone) {
        return userService.phoneIsAvailable(phone);
    }
}

