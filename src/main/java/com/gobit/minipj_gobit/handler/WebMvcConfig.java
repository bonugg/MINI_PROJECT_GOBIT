package com.gobit.minipj_gobit.handler;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //모든 페이지에서 실행
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TeamLeaderInterceptor())
                .addPathPatterns("/**");
    }
}