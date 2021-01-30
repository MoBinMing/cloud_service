package com.mobinming.cloud_service.shiro;

import cn.hutool.json.JSONUtil;
import com.mobinming.cloud_service.common.lang.Result;
import com.mobinming.cloud_service.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.SocketTimeoutException;

@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt)) {
            return null;
        }

        return new JwtToken(jwt);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt)) {
            //未带token的请求，AuthenticationInterceptor二次处理
            return true;
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            Subject subject = getSubject(request, httpResponse);
            if (subject.getPrincipal() == null) {
                // 校验jwt
                Claims claim = jwtUtils.getClaimByToken(jwt);
                if(claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                    // 这里判断是否为ajax
                    // 如果不是会走shiro默认的权限流程
                    if ("/logout".equals(request.getServletPath())) {
                        return true;
                    } else {
                        httpResponse.setContentType("application/json;charset=utf-8");
                        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
                        httpResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest)servletRequest).getHeader("Origin"));
                        httpResponse.getWriter().print(JSONUtil.toJsonStr(Result.fail(500,"token已失效，请重新登录",null)));
                    }
                    return false;
                    //throw new ExpiredCredentialsException("token已失效，请重新登录");
                }
            }
            try {
                //确保Redis处于启动状态、否则报错
                // 执行登录
                return executeLogin(servletRequest, servletResponse);
            }catch (Exception e){
                httpResponse.setContentType("application/json;charset=utf-8");
                httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
                httpResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest)servletRequest).getHeader("Origin"));
                httpResponse.getWriter().print(JSONUtil.toJsonStr(Result.fail(500,"服务器内部错误",null)));
                return false;
            }

        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result result = Result.fail(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result);

        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {

        }
        return false;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }

    /**
     * 判断ajax请求
     *
     * @param request
     * @return
     */
    private boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
    }
}