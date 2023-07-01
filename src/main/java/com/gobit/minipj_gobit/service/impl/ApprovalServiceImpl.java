package com.gobit.minipj_gobit.service.impl;

import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.repository.ApprovalRepository;
import com.gobit.minipj_gobit.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApprovalServiceImpl implements ApprovalService {
    ApprovalRepository approvalRepository;

    @Autowired
    public ApprovalServiceImpl(ApprovalRepository approvalRepository){
        this.approvalRepository = approvalRepository;
    }

    @Override
    public void saveApproval(Approval approval) {
        approvalRepository.save(approval);
        approvalRepository.flush();
    }

    //특정 결재 불러오기
    @Override
    public Approval getApproval(Integer appNum) {
        return approvalRepository.findById(appNum).get();
    }
}
