package com.gobit.minipj_gobit.noticeDept.repository;

import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import com.gobit.minipj_gobit.noticeDept.entity.nBoardNoticeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NfileRepository extends JpaRepository<nBoardNoticeFile, Long> {


    List<nBoardNoticeFile> findByBoard(nBoard board);
}
