package com.gobit.minipj_gobit.service;

import com.gobit.minipj_gobit.entity.Approval;

public interface ApprovalService {
    void saveApproval(Approval approval);

    Approval getApproval(long appNum);
    
}
