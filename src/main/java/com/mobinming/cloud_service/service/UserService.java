package com.mobinming.cloud_service.service;

import com.mobinming.cloud_service.common.dto.LoginDto;
import com.mobinming.cloud_service.common.dto.RegisterDto;
import com.mobinming.cloud_service.common.lang.Result;
import com.mobinming.cloud_service.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mbm
 * @since 2021-01-18
 */
public interface UserService extends IService<User> {
    Result login(LoginDto loginDto, HttpServletResponse response);
    Result register(RegisterDto registerDto);
    Result usernameIsAvailable(String aliasName);
    Result phoneIsAvailable(String phone);
    Result logout();
    Result searchUserByPhone(String phone, HttpServletResponse response);
}
