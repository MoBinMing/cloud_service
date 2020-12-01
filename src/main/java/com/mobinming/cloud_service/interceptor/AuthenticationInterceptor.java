package com.mobinming.cloud_service.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mobinming.cloud_service.entity.User;
import com.mobinming.cloud_service.service.UserService;
import com.mobinming.cloud_service.util.JwtUtils;
import com.mobinming.cloud_service.util.jwt.PassToken;
import com.mobinming.cloud_service.util.jwt.UserLoginToken;
import com.mobinming.cloud_service.util.jwt.WebLoginToken;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("Authorization");// 从 http 请求头中取出 token

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有WebLoginToken注释，有则跳过认证
        if (method.isAnnotationPresent(WebLoginToken.class)) {
            WebLoginToken webLoginToken = method.getAnnotation(WebLoginToken.class);
            if (webLoginToken.required()) {
                Cookie[] cookies = httpServletRequest.getCookies();
                if (cookies != null && cookies.length > 0) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("token")) {
                            token = cookie.getValue();
                            break;
                        }
                    }
                }
                Claims claim = jwtUtils.getClaimByToken(token);
                if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                    httpServletResponse.sendRedirect("/login");
                    return false;
                }
                return true;
            }
        }
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                Claims claim = jwtUtils.getClaimByToken(token);
                if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                    httpServletResponse.sendRedirect("/login");
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}