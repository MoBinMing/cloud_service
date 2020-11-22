package com.mobinming.cloud_service.controller;

import com.mobinming.cloud_service.entity.NoteCategory;
import com.mobinming.cloud_service.service.NoteCategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mbm
 * @since 2020-11-15
 */
@RestController
@RequestMapping("/cloud_service/note-category")
public class NoteCategoryController {
    @Autowired
    protected NoteCategoryService noteCategoryService;

    @GetMapping("/getAll")
    public List<NoteCategory> getAll() {
        return noteCategoryService.list();
    }
}

