package com.gobit.minipj_gobit.service;

import com.gobit.minipj_gobit.entity.Approval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApprovalService {
    void saveApproval(Approval approval);

    Approval getApproval(long appNum);

    Page<Approval> getApprovalList(Pageable pageable);

    void updateApproval(Approval approval);

    void deleteApproval(long appNum);

}
