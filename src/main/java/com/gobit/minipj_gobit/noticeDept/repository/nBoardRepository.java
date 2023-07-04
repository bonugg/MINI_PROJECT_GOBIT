package com.gobit.minipj_gobit.noticeDept.repository;

import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface nBoardRepository extends JpaRepository<nBoard, Long> {

    Page<nBoard> findAll(Pageable pageable);

    Page<nBoard> findByContentContainingOrTitleContaining(String searchKeyword, String keyword,Pageable pageable);

}

