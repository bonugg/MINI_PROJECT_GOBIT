package com.gobit.minipj_gobit;

import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.repository.dBoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MinipjGobitApplicationTests {

    @Autowired
    dBoardRepository dBoardRepository;
    @Test
    void contextLoads() {
    }

    @Test
    void 게시물_저장() {
        for (int i = 0; i < 55; i++) {
            String title = String.format("테스트 제목입니다:[%02d]", i);
            String content = "테스트 내용입니다~";
            dBoard board = new dBoard();
            board.setTitle(title);
            board.setContent(content);
            dBoardRepository.save(board);
        }
    }

}
