package com.gobit.minipj_gobit.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PostPersist;

import jakarta.persistence.PostUpdate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class UserOnOffEntityListener extends TextWebSocketHandler {
    private static List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private UserOnOff uonof;

    @PostPersist
    @PostUpdate
    public void onUserOnOffUpdate(UserOnOff userOnOff) {
        uonof = userOnOff;
        handleDatabaseChanges();
    }

    public void handleDatabaseChanges() {
        for (WebSocketSession currentSession : sessions) {
            if (currentSession.isOpen()) {
                CompletableFuture.runAsync(() -> {
                            try {
                                sendUserOnOffMessage(currentSession, uonof);
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

    public void sendUserOnOffMessage(WebSocketSession session, UserOnOff userOnOff) throws IOException {
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
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.add(session);
    }
}
