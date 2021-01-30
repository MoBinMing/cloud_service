package com.mobinming.cloud_service.controller.api;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mobinming.cloud_service.common.dto.LoginDto;
import com.mobinming.cloud_service.common.lang.Result;
import com.mobinming.cloud_service.entity.FriendRequest;
import com.mobinming.cloud_service.entity.User;
import com.mobinming.cloud_service.service.FriendRequestService;
import com.mobinming.cloud_service.service.UserService;
import com.mobinming.cloud_service.util.JwtUtils;
import com.mobinming.cloud_service.util.jwt.UserLoginToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 好友管理相关模块
 * </p>
 *
 * @author mbm
 * @since 2021-01-27
 */
@RestController
@RequestMapping("/api")
@Api(tags = "新好友相关模块")
public class FriendRequestController {
    @Resource
    protected UserService userService;
    @Resource
    protected FriendRequestService friendRequestService;
    @Autowired
    JwtUtils jwtUtils;


    @RequiresAuthentication
    @CrossOrigin
    @ApiOperation("我提出申请好友列表")
    @GetMapping(value = "/myRequestFriends")
    public Result myFriendRequests(HttpServletRequest request) {
        List<FriendRequest> list = friendRequestService.list(Wrappers.<FriendRequest>lambdaQuery()
                .eq(FriendRequest::getUserId, jwtUtils.getUserId(request)));
        return Result.succ("获取成功", list);
    }

    @RequiresAuthentication
    @CrossOrigin
    @ApiOperation("待审批好友请求列表")
    @GetMapping(value = "/friendRequestsMy")
    public Result friendRequestsMy(HttpServletRequest request) {
        List<FriendRequest> list = friendRequestService.list(Wrappers.<FriendRequest>lambdaQuery()
                .eq(FriendRequest::getRequestUserId, jwtUtils.getUserId(request)));
        return Result.succ("获取成功", list);
    }

    @RequiresAuthentication
    @CrossOrigin
    @ApiOperation("发起申请好友")
    @GetMapping(value = "/requestFriend")
    public Result requestFriend(@RequestParam("requestUserId") Integer requestUserId, HttpServletRequest request) {
        Integer userId = jwtUtils.getUserId(request);
        FriendRequest friendRequest = friendRequestService.getOne(Wrappers.<FriendRequest>lambdaQuery().eq(FriendRequest::getUserId, userId)
                .eq(FriendRequest::getRequestUserId, requestUserId), false);
        Assert.isNull(friendRequest, "以发起申请好友，请勿重复申请");
        friendRequest = new FriendRequest();
        friendRequest.setUserId(userId);
        friendRequest.setRequestUserId(requestUserId);
        friendRequest.setRequestTime(new Date());
        Assert.isFalse(friendRequestService.save(friendRequest), "发起申请好友失败，服务端错误");
        return Result.succ("发起申请好友成功");
    }

    @RequiresAuthentication
    @CrossOrigin
    @ApiOperation("同意好友申请")
    @GetMapping(value = "/agreeToFriendApplication")
    public Result agreeToFriendApplication(@RequestParam("agreeUserId") Integer agreeUserId, HttpServletRequest request) {
        Integer myId = jwtUtils.getUserId(request);
        FriendRequest friendRequest = friendRequestService.getOne(Wrappers.<FriendRequest>lambdaQuery().eq(FriendRequest::getUserId, agreeUserId)
                .eq(FriendRequest::getRequestUserId, myId), false);
        Assert.notNull(friendRequest, "同意好友申请失败，不存在该申请");
        Assert.isTrue(friendRequest.getState() == -1, "同意好友申请失败，请勿重复操作");
        friendRequest.setState(1);
        friendRequest.setAgreeTime(new Date());
        Assert.isTrue(friendRequest.updateById(), "同意好友申请失败，服务端错误");
        User user = userService.getById(myId);
        String[] fids = user.getFriendsId().split(",");
        if (!Arrays.asList(fids).contains(agreeUserId + "")) {
            user.setFriendsId(user.getFriendsId() + agreeUserId + ",");
        }
        return Result.succ(200, "通过好友成功", null);
    }

    @RequiresAuthentication
    @CrossOrigin
    @ApiOperation("拒绝好友申请")
    @GetMapping(value = "/rejectFriendRequest")
    public Result rejectFriendRequest(@RequestParam("rejectUserId") Integer rejectUserId, HttpServletRequest request) {
        Integer myId = jwtUtils.getUserId(request);
        FriendRequest friendRequest = friendRequestService.getOne(Wrappers.<FriendRequest>lambdaQuery().eq(FriendRequest::getUserId, rejectUserId)
                .eq(FriendRequest::getRequestUserId, myId), false);
        Assert.notNull(friendRequest, "不存在该好友申请");
        Assert.isTrue(friendRequest.getState() != 0, "请勿重复操作");
        friendRequest.setState(0);
        Assert.isTrue(friendRequest.updateById(), "服务器执行失败");
        return Result.succ(200, "拒绝好友成功", null);
    }
}

