package com.gobit.minipj_gobit.noticeDept.repository;

import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface nBoardRepository extends JpaRepository<nBoard, Long> {

    Page<nBoard> findAll(Pageable pageable);

    @Query(value = "select n from nBoard n order by n.regDate desc limit 5")
    List<nBoard> findBynBoardTop5();
}
