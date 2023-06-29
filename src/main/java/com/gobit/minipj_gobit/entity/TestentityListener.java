package com.gobit.minipj_gobit.entity;

import com.gobit.minipj_gobit.configuration.BeanUtils;
import com.gobit.minipj_gobit.handler.WSHandler;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;


@Transactional
@Component
public class TestEntityListener {

    @PostUpdate
    public void onUserOnOffUpdate(Testentity testentity) {
        if (testentity.getCheck().equals("승인")){
            WSHandler websocketHandler = BeanUtils.getBean(WSHandler.class);
            websocketHandler.handleDatabaseChanges(testentity);
        }
    }
}
