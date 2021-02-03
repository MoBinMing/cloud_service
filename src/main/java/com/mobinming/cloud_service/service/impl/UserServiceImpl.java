package com.mobinming.cloud_service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.gson.Gson;
import com.mobinming.cloud_service.common.dto.LoginDto;
import com.mobinming.cloud_service.common.dto.RegisterDto;
import com.mobinming.cloud_service.common.dto.UserDao;
import com.mobinming.cloud_service.common.lang.Result;
import com.mobinming.cloud_service.config.ServerConfig;
import com.mobinming.cloud_service.entity.User;
import com.mobinming.cloud_service.mapper.UserMapper;
import com.mobinming.cloud_service.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobinming.cloud_service.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mbm
 * @since 2021-01-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    JwtUtils jwtUtils;
    @Resource
    private ServerConfig serverConfig;

    @Override
    public Result login(LoginDto loginDto, HttpServletResponse response) {
        User user = getOne(new QueryWrapper<User>().eq("phone", loginDto.getPhone()));
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(user.getPassword().equals(loginDto.getPassword()), "账号或密码错误！");
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        user.setToken(jwt);
        UserDao userDao = BeanUtil.copyProperties(user, UserDao.class);
        // 用户可以另一个接口
        return Result.succ("登录成功", MapUtil.builder()
                .put("user", userDao)
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
        user.setRegisterTime(new Date());
        Assert.isTrue(save(user), "注册失败,插入数据量失败");
        return Result.succ("注册成功");
    }

    @Override
    public Result usernameIsAvailable(String aliasName) {
        Assert.notEmpty(aliasName, "参数aliasName值不能为空");
        User one = getOne(Wrappers.<User>lambdaQuery().eq(User::getAliasName, aliasName), false);
        Assert.isNull(one, "用户名不可用");
        return Result.succ("用户名可用");
    }

    @Override
    public Result phoneIsAvailable(String phone) {
        Assert.isTrue(isChinaPhoneLegal(phone), "参数phone非法");
        User one = getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone), false);
        Assert.isNull(one, "手机号被占用");
        return Result.succ("手机号可用");
    }

    @Override
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

    @Override
    public Result searchUserByPhone(String phone, HttpServletResponse response) {
        List<User> users = list(new QueryWrapper<User>().like("phone", phone));
        for (User u : users) {
            u.setPassword(null);
            u.setToken(null);
            u.setRegisterTime(null);
        }
        return Result.succ(users);
    }

    @Override
    public Result upBgImg(String base64Img, HttpServletRequest request) {
        Integer userId=jwtUtils.getUserId(request);
        JSONObject object = JSONObject.parseObject(base64Img);
        base64Img=object.getString("base64Img");
        Assert.isTrue(GenerateImage(base64Img,serverConfig.getBgImgPath(userId,"jpg")),"上传失败");
        return Result.succ("上传成功",serverConfig.getBgImgLinkUrl(userId,"jpg"));
    }

    // region 上传背景图片
    // 对字节数组字符串进行Base64解码并生成图片
    //imgFilePath 待保存的本地路径
    private static boolean GenerateImage(String base64Str, String imgFilePath) {
        if (base64Str == null) { // 图像数据为空
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(base64Str);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            //====
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // endregion
    public static boolean isChinaPhoneLegal(String str) {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        try {
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(str);
            return m.matches();
        } catch (Exception e) {
            return false;
        }

    }

}
