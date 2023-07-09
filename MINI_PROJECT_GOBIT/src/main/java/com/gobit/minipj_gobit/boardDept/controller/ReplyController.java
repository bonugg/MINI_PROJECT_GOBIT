package com.gobit.minipj_gobit.boardDept.controller;

import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.boardDept.entity.Reply;
import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.service.ReplyService;
import com.gobit.minipj_gobit.boardDept.service.dBoardService;
import com.gobit.minipj_gobit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final dBoardService boardService;
    private final ReplyService replyService;
    private final UserRepository userRepository;

    @PostMapping("/create/{id}")
    public String create(Model model, @PathVariable("id") Long id,
                              @RequestParam String content, Principal principal) {
        dBoard board = this.boardService.getBoard(id);
        User user = this.userRepository.findByUSERENO(Integer.parseInt(principal.getName())).get();

        Reply reply = this.replyService.create(board, content, user);

        return String.format("redirect:/boardDept/detail/%s#reply_%s", id, reply.getId());
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        Reply reply = this.replyService.getReply(id);
        this.replyService.delete(reply);
        return "redirect:/boardDept/detail/" + reply.getBoard().getId();
    }
}
