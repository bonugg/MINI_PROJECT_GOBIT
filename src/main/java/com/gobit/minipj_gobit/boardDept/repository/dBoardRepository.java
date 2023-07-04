package com.gobit.minipj_gobit.boardDept.repository;

import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface dBoardRepository extends JpaRepository<dBoard, Long> {
    Page<dBoard> findAll(Pageable pageable);

    @Query(value = "select d from dBoard d where d.user.USERDEPT = :dept order by d.createDate desc limit 6")
    List<dBoard> findBydBoardDept(String dept);

}
