package com.mobinming.cloud_service.controller.api;


import com.mobinming.cloud_service.common.lang.Result;
import com.mobinming.cloud_service.entity.Book;
import com.mobinming.cloud_service.service.BookService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mbm
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/api/")
public class BookApiController {
    @Autowired
    protected BookService service;

    @RequiresAuthentication
    @ApiOperation(value = "获取所有课本列表", notes = "获取所有课本列表")
    @GetMapping("getAllBook")
    public Result getBook() {
        List<Book> bookList=service.list();
        return Result.succ(200,"获取成功",bookList);
    }
}

