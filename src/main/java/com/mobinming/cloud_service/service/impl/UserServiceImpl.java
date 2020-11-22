package com.mobinming.cloud_service.service.impl;

import com.mobinming.cloud_service.entity.User;
import com.mobinming.cloud_service.mapper.UserMapper;
import com.mobinming.cloud_service.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mbm
 * @since 2020-11-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
