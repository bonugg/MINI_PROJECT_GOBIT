package com.gobit.minipj_gobit.boardDept.controller;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.service.ReplyService;
import com.gobit.minipj_gobit.boardDept.service.dBoardService;
import com.gobit.minipj_gobit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final dBoardService boardService;
    private final ReplyService replyService;
    private final UserRepository userRepository;

    @PostMapping("/create/{id}")
    public String createReply(Model model, @PathVariable("id") Long id,
                              @RequestParam String content, Principal principal) {
        dBoard board = this.boardService.getBoard(id);
        User user = this.userRepository.findByUSERENO(Integer.parseInt(principal.getName())).get();
        this.replyService.create(board, content, user);
        return "redirect:/boardDept/detail/" + id ;
    }
}
