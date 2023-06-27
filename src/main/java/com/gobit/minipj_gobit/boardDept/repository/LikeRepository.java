package com.gobit.minipj_gobit.boardDept.repository;

import com.gobit.minipj_gobit.boardDept.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByBoardIdAndUserId(Long boardId, Long userId);

    Integer countByBoardId(Long boardId);
}
