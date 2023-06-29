package com.gobit.minipj_gobit.entity;

import com.gobit.minipj_gobit.configuration.BeanUtils;
import com.gobit.minipj_gobit.handler.WSHandler;
import jakarta.persistence.PostPersist;

import jakarta.persistence.PostUpdate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class UserOnOffEntityListener extends TextWebSocketHandler {

    @PostPersist
    @PostUpdate
    public void onUserOnOffUpdate(UserOnOff userOnOff) {
        WSHandler websocketHandler = BeanUtils.getBean(WSHandler.class);
        websocketHandler.handleDatabaseChanges(userOnOff);
    }
}
