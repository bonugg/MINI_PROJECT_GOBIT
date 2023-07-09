package com.gobit.minipj_gobit.service.impl;

import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.repository.ApprovalRepository;
import com.gobit.minipj_gobit.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApprovalServiceImpl implements ApprovalService {
    private ApprovalRepository approvalRepository;

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

    @Override
    public Page<Approval> getApprovalList(Pageable pageable) {
        return approvalRepository.findAll(pageable);
    }

    //결재리스트 불러오기
    public Page<Approval> getApprovalList(Pageable pageable, String dept) {
        return approvalRepository.findByDept(pageable, dept);
    }

    @Override
    public void updateApproval(Approval approval) {
        approvalRepository.save(approval);
        approvalRepository.flush();
    }


    @Override
    public void deleteApproval(long appNum) {
        approvalRepository.deleteById(appNum);
        approvalRepository.deleteByAppNum(appNum);
        approvalRepository.flush();
    }

    // // 2023.07.05 전체 결재문서, 승인 대기 문서, 반려 문서, 승인 완료문서
    @Override
    public int cntTotalApp() {
        return approvalRepository.cntTotalApp();
    }

    @Override
    public int cntWaitApp() {
        return approvalRepository.cntWaitApp();
    }

    @Override
    public int cntRejectApp() {
        return approvalRepository.cntRejectApp();
    }

    @Override
    public int cntFinApp() {
        return approvalRepository.cntFinApp();
    }








}
