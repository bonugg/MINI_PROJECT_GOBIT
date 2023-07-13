package com.gobit.minipj_gobit.entity;

import com.gobit.minipj_gobit.configuration.BeanUtils;
import com.gobit.minipj_gobit.handler.WSHandler;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;


@Component
public class MessageListener extends TextWebSocketHandler {
    @Autowired
    private HttpSession httpSession;
    @PostPersist
    public void onMessageSend(Message message) {
        User user = (User) httpSession.getAttribute("user");
        if(message.getUser().getUSERNUM() == user.getUSERNUM() || message.getReceiveUser().getUSERNUM() == user.getUSERNUM()){
            WSHandler websocketHandler = BeanUtils.getBean(WSHandler.class);
            websocketHandler.handleDatabaseChanges(message);
        }
    }

    @PostUpdate
    public void onMessageUpdate(Message message) {
        User user = (User) httpSession.getAttribute("user");
        if(message.getUser().getUSERNUM() == user.getUSERNUM() || message.getReceiveUser().getUSERNUM() == user.getUSERNUM()){
            WSHandler websocketHandler = BeanUtils.getBean(WSHandler.class);
            websocketHandler.handleDatabaseChanges(message);
        }
    }


    // 새로운 메소드 추가
    public void afterSaveAll(List<Message> messages) {
        if (!messages.isEmpty()) {
            Message lastMessage = messages.get(messages.size() - 1);
            onMessageUpdate(lastMessage);
        }
    }
}
