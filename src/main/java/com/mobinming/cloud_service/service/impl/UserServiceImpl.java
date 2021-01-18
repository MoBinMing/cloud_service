package com.mobinming.cloud_service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mobinming.cloud_service.common.dto.LoginDto;
import com.mobinming.cloud_service.common.dto.RegisterDto;
import com.mobinming.cloud_service.common.lang.Result;
import com.mobinming.cloud_service.entity.User;
import com.mobinming.cloud_service.mapper.UserMapper;
import com.mobinming.cloud_service.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobinming.cloud_service.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mbm
 * @since 2021-01-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    JwtUtils jwtUtils;
    @Override
    public Result login(LoginDto loginDto, HttpServletResponse response) {
        User user = getOne(new QueryWrapper<User>().eq("phone", loginDto.getPhone()));
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(user.getPassword().equals(loginDto.getPassword()), "账号或密码错误！");
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        // 用户可以另一个接口
        return Result.succ("登录成功", MapUtil.builder()
                .put("user", user)
                .put("token", jwt)
                .map()
        );
    }

    @Override
    public Result register(RegisterDto registerDto) {
        User userP = getOne(new QueryWrapper<User>().eq("phone", registerDto.getPhone()));
        User userU = getOne(Wrappers.<User>lambdaQuery().eq(User::getAliasName, registerDto.getAliasName()), false);
        Assert.isNull(userP, "注册失败,手机号:" + registerDto.getPhone() + "被占用");
        Assert.isNull(userU, "注册失败，用户名:" + registerDto.getAliasName() + "被占用");
        User user = new User();
        BeanUtil.copyProperties(registerDto, user);
        user.setRegisterTime(LocalDateTime.now());
        Assert.isTrue(save(user), "注册失败,插入数据量失败");
        return Result.succ("注册成功");
    }

    @Override
    public Result usernameIsAvailable(String aliasName) {
        Assert.notNull(aliasName, "参数aliasName不能为空");
        User one = getOne(Wrappers.<User>lambdaQuery().eq(User::getAliasName, aliasName), false);
        Assert.isNull(one, "用户名不可用");
        return Result.succ("用户名可用");
    }

    @Override
    public Result phoneIsAvailable(String phone) {
        Assert.notNull(phone, "参数phone不能为空");
        User one = getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone), false);
        Assert.isNull(one,"手机号被占用");
        return Result.succ("手机号可用");
    }

    @Override
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }
}
