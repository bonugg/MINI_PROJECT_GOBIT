package com.gobit.minipj_gobit.entity;

import com.gobit.minipj_gobit.configuration.BeanUtils;
import com.gobit.minipj_gobit.handler.WSHandler;
import jakarta.persistence.PostPersist;

import jakarta.persistence.PostUpdate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class UserOnOffentityListener extends TextWebSocketHandler {
    private UserOnOff uonof;

    @PostPersist
    @PostUpdate
    public void onUserOnOffUpdate(UserOnOff userOnOff) {
        uonof = userOnOff;
        WSHandler websocketHandler = BeanUtils.getBean(WSHandler.class);
        websocketHandler.handleDatabaseChanges(userOnOff);
    }
}
