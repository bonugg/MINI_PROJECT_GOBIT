package com.gobit.minipj_gobit.configuration;


import com.gobit.minipj_gobit.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final ApprovalRepository approvalRepository;

    @Value("${file.resource}")
    private String filePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
        resourceHandlerRegistry.addResourceHandler("/upload/**")
                .addResourceLocations(filePath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TeamLeaderInterceptor(approvalRepository))
                .excludePathPatterns("/login")    // 로그인 관련 요청은 제외
                .excludePathPatterns("/login/**")    // 로그인 관련 요청은 제외
                .excludePathPatterns("/signup")    // 회원가입 관련 요청은 제외
                .excludePathPatterns("/css/**") // 정적 리소스 요청은 제외
                .excludePathPatterns("/js/**") // 정적 리소스 요청은 제외
                .excludePathPatterns("/img/**") // 정적 리소스 요청은 제외
                .excludePathPatterns("/error")    // 오류 라우팅을 제외
                .addPathPatterns("/**");

    }
}