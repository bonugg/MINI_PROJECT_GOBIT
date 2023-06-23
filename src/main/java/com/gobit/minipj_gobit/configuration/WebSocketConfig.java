package com.gobit.minipj_gobit.configuration;

import com.gobit.minipj_gobit.Handler.DatabaseChangeHandler;
import com.gobit.minipj_gobit.service.DatabaseWatcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig  implements WebSocketConfigurer{
    private final DatabaseWatcherService databaseWatcherService;
    private final ScheduledExecutorService executor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(databaseChangeHandler(), "/database-change")
                .setAllowedOrigins("*"); // 사용자의 요구 사항에 따라 허용되는 오리진 변경
    }
    @Bean
    public WebSocketHandler databaseChangeHandler() {
        return new DatabaseChangeHandler(databaseWatcherService, executor);
    }
}
