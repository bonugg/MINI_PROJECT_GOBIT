package com.gobit.minipj_gobit.service.impl;

import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.repository.ApprovalRepository;
import com.gobit.minipj_gobit.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Approval getApproval(long appNum) {
        if(approvalRepository.findByAppNum(appNum).isEmpty()){
            return null;
        }
        return approvalRepository.findByAppNum(appNum).get();
    }

    //결재리스트 불러오기
    public Page<Approval> getApprovalList(Pageable pageable) {
        return approvalRepository.findAll(pageable);
    }

    @Override
    public void updateApproval(Approval approval) {
        approvalRepository.save(approval);
        approvalRepository.flush();
    }

    @Override
    public void deleteApproval(long appNum) {
        approvalRepository.deleteById(appNum);
        approvalRepository.flush();
    }
}
