package com.gobit.minipj_gobit.entity;

import com.gobit.minipj_gobit.configuration.BeanUtils;
import com.gobit.minipj_gobit.handler.WSHandler;
import jakarta.persistence.PostUpdate;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Transactional
@Component
public class ApprovalListener {

    @PostUpdate
    public void AppUpdate(Approval approval){
        if(approval.getAppState().equals("승인")){
            WSHandler websocketHandler = BeanUtils.getBean(WSHandler.class);
            websocketHandler.handleDatabaseChanges(approval);
        }
    }

}
