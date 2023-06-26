package com.gobit.minipj_gobit.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobit.minipj_gobit.Entity.UserOnOff;
import com.gobit.minipj_gobit.service.DatabaseWatcherService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


@RequiredArgsConstructor
public class DatabaseChangeHandler extends TextWebSocketHandler {
    private final DatabaseWatcherService databaseWatcherService;
    private final ScheduledExecutorService executor;
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);

        executor.scheduleAtFixedRate(() -> {
            Optional<LocalDateTime> updatedTime = databaseWatcherService.checkForDatabaseChange();
            for (WebSocketSession currentSession : sessions) {
                if (updatedTime.isPresent() && currentSession.isOpen()) {
                    Optional<UserOnOff> userOnOff = databaseWatcherService.findByUPDATEAT(updatedTime.get());
                    if (userOnOff.isPresent()) {
                        CompletableFuture.runAsync(() -> {
                                    try {
                                        sendUserOnOffMessage(currentSession, userOnOff.get());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .exceptionally(t -> {
                                            System.out.println("Error sending message: " + t.getMessage());
                                            return null;
                                        }
                                );
                    }
                }
            }

        }, 0, 3, TimeUnit.SECONDS); // 3초마다 실행
    }

    private void sendUserOnOffMessage(WebSocketSession session, UserOnOff userOnOff) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("usernum", userOnOff.getUser().getUSERNUM());
        result.put("username", userOnOff.getUser().getUSERNAME());
        result.put("userdept", userOnOff.getUser().getUSERDEPT());
        result.put("start", userOnOff.getSTART());
        result.put("end", userOnOff.getEND());

        ObjectMapper objectMapper = new ObjectMapper();
        String userOnOffJson = objectMapper.writeValueAsString(result);
        session.sendMessage(new TextMessage(userOnOffJson));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}