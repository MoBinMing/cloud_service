package com.mobinming.cloud_service.controller.api;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mobinming.cloud_service.common.dto.LoginDto;
import com.mobinming.cloud_service.common.dto.RegisterDto;
import com.mobinming.cloud_service.common.lang.Result;
import com.mobinming.cloud_service.config.ServerConfig;
import com.mobinming.cloud_service.entity.User;
import com.mobinming.cloud_service.service.UserService;
import com.mobinming.cloud_service.util.JwtUtils;
import com.mobinming.cloud_service.util.jwt.PassToken;
import com.mobinming.cloud_service.util.jwt.UserLoginToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javafx.util.Pair;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.MalformedURLException;
import java.util.Date;

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

    @PassToken
    @CrossOrigin
    @ApiOperation("用户登录")
    @PostMapping(value = "/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        return userService.login(loginDto, response);
    }

    @RequiresAuthentication
    @ApiOperation(value = "用户推出登录", notes = "这是用户推出登录接口")
    @GetMapping("/logout")
    public Result logout() {
        return userService.logout();
    }

    @PassToken
    @CrossOrigin
    @ApiOperation(value = "用户注册", notes = "这是用户注册接口")
    //用@RequestBody取值ajax用json字符串
    //表单去除@RequestBody
    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterDto registerDto) {
        return userService.register(registerDto);
    }

    @PassToken
    @CrossOrigin
    @ApiOperation(value = "用户名是否可用", notes = "检查用户名是否被占用")
    @GetMapping("/usernameIsAvailable")
    public Result usernameIsAvailable(@RequestParam("aliasName") String aliasName) {
        return userService.usernameIsAvailable(aliasName);
    }

    @PassToken
    @CrossOrigin
    @ApiOperation(value = "手机号是否可用", notes = "检查手机号是否被占用")
    @GetMapping("/phoneIsAvailable")
    public Result phoneIsAvailable(@RequestParam("phone") String phone) {
        return userService.phoneIsAvailable(phone);
    }

    @RequiresAuthentication
    @CrossOrigin
    @ApiOperation("搜索用户")
    @GetMapping(value = "/searchUserByPhone")
    public Result searchUserByPhone(@RequestParam("phone") String phone, HttpServletResponse response) {
        return userService.searchUserByPhone(phone, response);
    }


    @RequiresAuthentication
    @CrossOrigin
    @ApiOperation("上传背景图片")
    @PostMapping(value = "/upBgImg", consumes = "application/json", produces = "application/json;charset=UTF-8")
    @ResponseBody // 返回给请求着仅仅body的内容
    public Result upBgImg(@RequestBody String base64Img, HttpServletRequest request) {
        return userService.upBgImg(base64Img,request);
    }

}

