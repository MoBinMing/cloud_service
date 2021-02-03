package com.mobinming.cloud_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload-bg-path}")
    private String upload_bg_path;
    @Value("${upload-head-path}")
    private String upload_head_path;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/bg/**").addResourceLocations("file:"+upload_bg_path);
        registry.addResourceHandler("/img/head/**").addResourceLocations("file:"+upload_head_path);
    }
}