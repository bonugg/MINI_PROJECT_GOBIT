package com.gobit.minipj_gobit.repository;

import com.gobit.minipj_gobit.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    Optional<Approval> findByAppNum(long appNum);
}