package com.gobit.minipj_gobit.boardDept.repository;

import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface dBoardRepository extends JpaRepository<dBoard, Long> {
    Page<dBoard> findAll(Pageable pageable);

    Page<dBoard> findAll(Specification<dBoard> spec, Pageable pageable);
}
