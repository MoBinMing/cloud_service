package com.mobinming.cloud_service.common;

import com.mobinming.cloud_service.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException e) {
        log.error("运行时异常：----------------{}", e);
        return Result.fail( "ShiroException运行时异常：\n" + e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        log.error("实体校验异常：----------------{}", e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();

        return Result.fail("实体校验异常：\n" + objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        log.error("Assert异常：----------------{}", e);
        return Result.fail( "Assert异常：\n"+e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e) {
        log.error("运行时异常：----------------{}", e);
        return Result.fail("服务器运行时异常：\n"+e.getMessage());
    }

}
