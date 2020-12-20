package com.mobinming.cloud_service.service;

import com.mobinming.cloud_service.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mbm
 * @since 2020-11-15
 */
public interface BookService extends IService<Book> {
    List<Book> getList();
}
