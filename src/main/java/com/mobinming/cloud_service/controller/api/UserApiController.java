package com.mobinming.cloud_service.controller.api;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mobinming.cloud_service.common.dto.LoginDto;
import com.mobinming.cloud_service.common.lang.Result;
import com.mobinming.cloud_service.entity.Book;
import com.mobinming.cloud_service.entity.User;
import com.mobinming.cloud_service.service.BookService;
import com.mobinming.cloud_service.service.UserService;
import com.mobinming.cloud_service.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
            return Result.fail("密码错误！");
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
}

