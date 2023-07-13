package com.gobit.minipj_gobit.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.Message;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.entity.UserOnOff;
import com.gobit.minipj_gobit.repository.ApprovalRepository;
import com.gobit.minipj_gobit.repository.MessageRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class WSHandler extends TextWebSocketHandler {
    private final ApprovalRepository approvalRepository;
    private final MessageRepository messageRepository;

    private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void handleDatabaseChanges(Object o) {
        for (WebSocketSession currentSession : sessions.values()) {
            if (currentSession.isOpen()) {
                System.out.println(currentSession + "===");
                String sessionUri = currentSession.getUri().toString();
                CompletableFuture.runAsync(() -> {
                            try {
                                if (o instanceof UserOnOff && sessionUri.contains("dept=")) {
                                    String dept = sessionUri.substring(sessionUri.indexOf("dept=") + 5);
                                    UserOnOff userOnOff = (UserOnOff) o;
                                    if (dept.equals(userOnOff.getUser().getUSERDEPT())) {
                                        sendUserOnOffMessage2(currentSession, o);
                                        System.out.println("출퇴근 메세지 전송 성공");
                                    }
                                } else if (o instanceof Approval && sessionUri.contains("dept=")) {
                                    String dept = sessionUri.substring(sessionUri.indexOf("dept=") + 5);
                                    Approval approval = (Approval) o;
                                    if (dept.equals(approval.getUserNum().getUSERDEPT())) {
                                        sendUserOnOffMessage2(currentSession, o);
                                        System.out.println("결재서 메세지 전송 성공");
                                    }
                                } else if (o instanceof Message && sessionUri.contains("charRoom=")) {
                                    Message message = (Message) o;
                                    int charRoom = Integer.parseInt(sessionUri.substring(sessionUri.indexOf("charRoom=") + 9));
                                    if (charRoom == message.getReceiveUser().getUSERNUM() + message.getUser().getUSERNUM()) {

                                        Map<String, Integer> uriCountMap = new HashMap<>();

                                        // URI별로 카운트
                                        for (WebSocketSession session : sessions.values()) {
                                            String uri = session.getUri().toString();
                                            uriCountMap.put(uri, uriCountMap.getOrDefault(uri, 0) + 1);
                                        }

                                        // 동일한 URI에 접속한 세션들에 메시지 전송
                                        boolean messageSent = false;
                                        for (WebSocketSession session : sessions.values()) {
                                            String uri = session.getUri().toString();

                                            if (uriCountMap.get(uri) >= 2 && session.isOpen()) {
                                                try {
                                                    sendUserOnOffMessagetwo(currentSession, o);
                                                    System.out.println("개인 메세지 전송 성공 두놈");
                                                    messageSent = true;
                                                    break;
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        // 두 개의 세션을 타겟팅하여 메시지를 전송한 경우만 아래 코드를 실행하지 않음
                                        if (!messageSent) {
                                            sendUserOnOffMessage(currentSession, o);
                                            System.out.println("개인 메세지 전송 성공");
                                        }
                                    }
                                } else if (o instanceof Message && sessionUri.contains("userNum=")) {
                                    Message message = (Message) o;
                                    int requestedUserNum = Integer.parseInt(sessionUri.substring(sessionUri.indexOf("userNum=") + 8));
                                    if (requestedUserNum == message.getReceiveUser().getUSERNUM()) {
                                        sendUserOnOffMessage(currentSession, o);
                                        System.out.println("메세지 알림 전송 성공");
                                    }
                                }
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

        if (o instanceof Message) {
            Message message = (Message) o;
            result.put("sendid", message.getUser().getUSERNUM());
            result.put("sendMsg", message.getChatSend());
            result.put("sendUser", message.getUser().getUSERNAME());
            result.put("receiveUser", message.getReceiveUser().getUSERNUM());
            result.put("sendUserImg", message.getUser().getUSERIMAGE());
            result.put("sendUserCnt", messageRepository.findBycnt(message.getUser(), message.getReceiveUser()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
            LocalDateTime chatSendDate = message.getChatSendDate();
            String sendDate = chatSendDate.format(formatter);
            result.put("sendDate", sendDate);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String Json = objectMapper.writeValueAsString(result);
        session.sendMessage(new TextMessage(Json));
    }

    public void sendUserOnOffMessagetwo(WebSocketSession session, Object o) throws IOException {
        Map<String, Object> result = new HashMap<>();

        if (o instanceof Message) {
            Message message = (Message) o;
            result.put("sendid", message.getUser().getUSERNUM());
            result.put("sendMsg", message.getChatSend());
            result.put("sendUser", message.getUser().getUSERNAME());
            result.put("receiveUser", message.getReceiveUser().getUSERNUM());
            result.put("sendUserImg", message.getUser().getUSERIMAGE());
            result.put("sendUserCnt", messageRepository.findBycnt(message.getUser(), message.getReceiveUser()));
            result.put("usercnt", "2");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
            LocalDateTime chatSendDate = message.getChatSendDate();
            String sendDate = chatSendDate.format(formatter);
            result.put("sendDate", sendDate);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String Json = objectMapper.writeValueAsString(result);
        session.sendMessage(new TextMessage(Json));
    }

    public void sendUserOnOffMessage2(WebSocketSession session, Object o) throws IOException {
        Map<String, Object> result = new HashMap<>();

        if (o instanceof UserOnOff) {
            UserOnOff userOnOff = (UserOnOff) o;
            result.put("usernum", userOnOff.getUser().getUSERNUM());
            result.put("username", userOnOff.getUser().getUSERNAME());
            result.put("userdept", userOnOff.getUser().getUSERDEPT());
            result.put("start", userOnOff.getSTART());
            result.put("end", userOnOff.getEND());
        } else if (o instanceof Approval) {
            Approval approval = (Approval) o;
            result.put("testcnt", approvalRepository.findByCntUserApp(approval.getUserNum()));
            result.put("testusernum", approval.getUserNum().getUSERNUM());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String Json = objectMapper.writeValueAsString(result);
        session.sendMessage(new TextMessage(Json));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        handleJoinLeaveMessage("left", session); // 퇴장 메시지 처리
        sessions.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("연결된 세션 ID: " + session.getId());
        sessions.put(session.getId(), session);  // 세션을 추가해야 다른 사용자가 알 수 있음
        handleJoinLeaveMessage("joined", session); // 입장 메시지 처리
        printSessions();
        System.out.println("연결된 세션 ID: " + session.getId());
        printSessions();
    }

    public int getActiveSessionCount() {
        return sessions.size();
    }

    public void printSessions() {
        System.out.println("현재 세션 목록:");
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            System.out.println("ID: " + entry.getKey() + ", WebSocketSession: " + entry.getValue());
        }
    }

    public void handleJoinLeaveMessage(String status, WebSocketSession currentSession) {
        String sessionUri = currentSession.getUri().toString();

        if (sessionUri.contains("charRoom=")) {
            int charRoom = Integer.parseInt(sessionUri.substring(sessionUri.indexOf("charRoom=") + 9));
            System.out.println(charRoom+"방번호");

            for (WebSocketSession session : sessions.values()) {
                String uri = session.getUri().toString();
                if (uri.contains("charRoom=")) {
                    int sessionCharRoom = Integer.parseInt(uri.substring(uri.indexOf("charRoom=") + 9));

                    if (charRoom == sessionCharRoom && session.isOpen()) {
                        try {
                            String message = status;
                            session.sendMessage(new TextMessage(message));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
