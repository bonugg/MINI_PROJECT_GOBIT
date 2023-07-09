package com.gobit.minipj_gobit.repository;

import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    Optional<Approval> findByAppNum(long appNum);

    @Query(value = "select a from Approval a where a.userDept = :dept")
    Page<Approval> findByDept(Pageable pageable, String dept);


    @Query(value = "select count(a.appAlarm) from Approval a where a.userNum = :user and a.appState = '승인' and  a.appAlarm = 0")
    int findByCntUserApp(User user);

    void deleteByAppNum(long appNum);

    @Query(value = "select count(*) from Approval")
    int cntTotalApp();

    @Query(value = "select count(*) from Approval where appState = '미승인'")
    int cntWaitApp();

    @Query(value = "select count(*) from Approval where appState = '반려'")
    int cntRejectApp();

    @Query(value = "select count(*) from Approval where appState = '승인'")
    int cntFinApp();
}