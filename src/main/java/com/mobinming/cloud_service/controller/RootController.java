package com.mobinming.cloud_service.controller;


import com.mobinming.cloud_service.util.jwt.WebLoginToken;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mbm
 * @since 2020-11-06
 */
@Controller
@RequestMapping("/")
public class RootController {
    //    @Autowired
    //    private HttpServletRequest request;
    @WebLoginToken
    @GetMapping("home")
    public String home() {
        return "home";
    }
    @GetMapping("login")
    public String login() {
        return "login";
    }
}

