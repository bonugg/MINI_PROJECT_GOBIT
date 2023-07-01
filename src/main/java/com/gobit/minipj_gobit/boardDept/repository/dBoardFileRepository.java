package com.gobit.minipj_gobit.boardDept.repository;

import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.entity.dBoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface dBoardFileRepository extends JpaRepository<dBoardFile, Long> {
    List<dBoardFile> findAllByBoardId(Long boardId);
    List<dBoardFile> findAllByBoard(dBoard board);
    List<dBoardFile> findAllByIdIn(List<Long> ids);
    void deleteAllByIdIn(List<Long> ids);
}
