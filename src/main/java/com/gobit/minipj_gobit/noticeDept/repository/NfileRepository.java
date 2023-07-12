package com.gobit.minipj_gobit.noticeDept.repository;

import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import com.gobit.minipj_gobit.noticeDept.entity.nBoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NfileRepository extends JpaRepository<nBoardFile, Long> {


    List<nBoardFile> findAllByBoard(nBoard board);

    @Query(value = "select n from nBoardFile n where n.board.id = : bid")
    List<nBoardFile> findAllByBoard2(Long bid);
}
