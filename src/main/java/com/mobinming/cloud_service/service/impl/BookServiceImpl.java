package com.mobinming.cloud_service.service.impl;

import com.mobinming.cloud_service.entity.Book;
import com.mobinming.cloud_service.mapper.BookMapper;
import com.mobinming.cloud_service.service.BookService;
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
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

}
