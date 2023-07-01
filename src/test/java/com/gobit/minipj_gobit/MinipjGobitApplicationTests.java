package com.gobit.minipj_gobit;

import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.repository.dBoardRepository;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.time.LocalDateTime;

@SpringBootTest
class MinipjGobitApplicationTests {

    @Autowired
    dBoardRepository dBoardRepository;
    @Autowired
    UserRepository userRepository;
    @Test
    void contextLoads() {
    }

//    @Test
//    void 게시물_저장() {
//        for (int i = 0; i < 55; i++) {
//            String title = String.format("테스트 제목입니다:[%02d]", i);
//            String content = "테스트 내용입니다~";
//            User user = userRepository.findByUSERENO(20230010).get();
//
//            dBoard board = dBoard.builder()
//                    .title(title)
//                    .content(content)
//                    .user(user)
//                    .createDate(LocalDateTime.now())
//                    .modifyDate(LocalDateTime.now())
//                    .cnt(0)
//                    .like(0)
//                    .build();
//            dBoardRepository.save(board);
//        }
//    }

    @Test
    void 파일_삭제() {
        String saveName = "/Users/minje/Desktop/upload/728209ea-1560-4fa7-9258-b8f1330cf5e9.png";
        File deleteFile = new File(saveName);
        System.out.println("존재하는지: " + deleteFile.exists());
        if (deleteFile.exists()) {
            System.out.println("삭제했는지" + deleteFile.delete()); // 파일 삭제
        }
    }
}
