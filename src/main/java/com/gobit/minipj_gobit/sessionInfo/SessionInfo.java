package com.gobit.minipj_gobit.sessionInfo;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
public class SessionInfo {
    private final WebSocketSession session;
    private final String department;

    public SessionInfo(WebSocketSession session, String department) {
        this.session = session;
        this.department = department;
    }

    // Getters and Setters
}