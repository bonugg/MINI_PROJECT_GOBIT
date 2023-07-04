package com.gobit.minipj_gobit.repository;

import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    Optional<Approval> findByAppNum(long appNum);

    @Query(value = "select count(a.appAlarm) from Approval a where a.userNum = :user and a.appState = '승인' and  a.appAlarm = 0")
    int findByCntUserApp(User user);
}