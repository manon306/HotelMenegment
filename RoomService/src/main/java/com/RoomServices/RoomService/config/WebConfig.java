package com.RoomServices.RoomService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ربط أي طلب يبدأ بـ /images/ بالمجلد الفعلي
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/app/uploads/");
    }
}
