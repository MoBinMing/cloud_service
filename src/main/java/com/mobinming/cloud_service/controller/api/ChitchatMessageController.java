package com.mobinming.cloud_service.controller.api;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mobinming.cloud_service.common.lang.Result;
import com.mobinming.cloud_service.entity.ChitchatMessage;
import com.mobinming.cloud_service.entity.FriendRequest;
import com.mobinming.cloud_service.service.ChitchatMessageService;
import com.mobinming.cloud_service.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mbm
 * @since 2021-01-28
 */
@RestController
@RequestMapping("/api")
@Api(tags = "聊天信息相关模块")
public class ChitchatMessageController {
    @Resource
    ChitchatMessageService chitchatMessageService;
    @Resource
    JwtUtils jwtUtils;
    @RequiresAuthentication
    @CrossOrigin
    @ApiOperation("获取新消息")
    @GetMapping(value = "/getMessages")
    public Result getChitchatMessages(HttpServletRequest request) {
        List<ChitchatMessage> list = chitchatMessageService.list(Wrappers.<ChitchatMessage>lambdaQuery()
                .eq(ChitchatMessage::getToUserId, jwtUtils.getUserId(request)).eq(ChitchatMessage::getIsSend,0));
        return Result.succ("获取成功", list);
    }
}

