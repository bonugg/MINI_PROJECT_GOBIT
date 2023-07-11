package com.gobit.minipj_gobit.service.impl;

import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.ApprovalRepository;
import com.gobit.minipj_gobit.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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


    //검색기능 구현
    @Override
    public Page<Approval> searchAppLeaderDept(Pageable pageable, String dept, String sWord) {
        return approvalRepository.findByUserDeptAndAppContentContaining(pageable, dept, sWord);
    }

    @Override
    public Page<Approval> searchAppUser(Pageable pageble, User user, String sWord) {
        return approvalRepository.findByUserNumAndAppContentContaining(pageble, user, sWord);
    }

    //결재리스트 불러오기
    @Override
    public Page<Approval> findByDept(Pageable pageable, String dept) {
        return approvalRepository.findByDept(pageable, dept);
    }

    @Override
    public Page<Approval> findByUser(Pageable pageable, User user) {
        return approvalRepository.findByUser(pageable, user);
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
    public int cntLeadTotalApp(String dept) {
        return approvalRepository.cntLeadTotalApp(dept);
    }
    @Override
    public int cntLeadWaitApp(String dept) {
        return approvalRepository.cntLeadWaitApp(dept);
    }
    @Override
    public int cntLeadRejectApp(String dept) {
        return approvalRepository.cntLeadRejectApp(dept);
    }
    @Override
    public int cntLeadFinApp(String dept) {
        return approvalRepository.cntLeadFinApp(dept);
    }
    @Override
    public int cntMemTotalApp(User user) {
        return approvalRepository.cntMemTotalApp(user);
    }
    @Override
    public int cntMemWaitApp(User user) {
        return approvalRepository.cntMemWaitApp(user);
    }
    @Override
    public int cntMemRejectApp(User user) {
        return approvalRepository.cntMemRejectApp(user);
    }
    @Override
    public int cntMemFinApp(User user) {
        return approvalRepository.cntMemFinApp(user);
    }

    @Override
    public long getAppVacReq(long appNum) {
        approvalRepository.findAppVacReqByAppNum(appNum);
        return approvalRepository.findAppVacReqByAppNum(appNum);
    }

}
