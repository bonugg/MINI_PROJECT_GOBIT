package com.gobit.minipj_gobit.boardDept.service;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.boardDept.entity.Reply;
import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    public void create(dBoard board, String content, User user) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setModifyDate(LocalDateTime.now());
        reply.setBoard(board);
        reply.setUser(user);
        this.replyRepository.save(reply);
    }
}
