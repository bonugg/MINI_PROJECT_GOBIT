package com.gobit.minipj_gobit.service;

import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApprovalService {
    void saveApproval(Approval approval);

    Approval getApproval(long appNum);

    Page<Approval> findByDept(Pageable pageable, String dept);

    Page<Approval> findByUser(Pageable pageable, User user);

    Page<Approval> searchAppLeaderDept(Pageable pageable,String sWord, String dept);

    Page<Approval> searchAppUser(Pageable pageble, User user, String sWord);
    void updateApproval(Approval approval);

    void deleteApproval(long appNum);

    int cntLeadTotalApp(String dept);
    int cntLeadWaitApp(String dept);
    int cntLeadRejectApp(String dept);
    int cntLeadFinApp(String dept);


    int cntMemTotalApp(User user);
    int cntMemWaitApp(User user);
    int cntMemRejectApp(User user);
    int cntMemFinApp(User user);


}
