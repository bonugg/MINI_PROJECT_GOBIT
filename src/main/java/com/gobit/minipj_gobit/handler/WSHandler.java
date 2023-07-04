package com.gobit.minipj_gobit.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.UserOnOff;
import com.gobit.minipj_gobit.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
public class WSHandler extends TextWebSocketHandler {
    private final ApprovalRepository approvalRepository;

    private static List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void handleDatabaseChanges(Object o) {
        for (WebSocketSession currentSession : sessions) {
            if (currentSession.isOpen()) {
                CompletableFuture.runAsync(() -> {
                            try {
                                System.out.println("메세지 전송 전");
                                sendUserOnOffMessage(currentSession, o);
                                System.out.println("메세지 전송 성공");
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

    public void sendUserOnOffMessage(WebSocketSession session, Object o) throws IOException {
        Map<String, Object> result = new HashMap<>();

        if (o instanceof UserOnOff) {
            UserOnOff userOnOff = (UserOnOff) o;
            result.put("usernum", userOnOff.getUser().getUSERNUM());
            result.put("username", userOnOff.getUser().getUSERNAME());
            result.put("userdept", userOnOff.getUser().getUSERDEPT());
            result.put("start", userOnOff.getSTART());
            result.put("end", userOnOff.getEND());
        }else if(o instanceof Approval){
            Approval approval = (Approval) o;
            result.put("testcnt", approvalRepository.findByCntUserApp(approval.getUser()));
            result.put("testusernum", approval.getUser().getUSERNUM());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String Json = objectMapper.writeValueAsString(result);
        session.sendMessage(new TextMessage(Json));
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
