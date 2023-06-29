package com.gobit.minipj_gobit.noticeDept.controller;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.boardDept.entity.BoardForm;
import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.noticeDept.dto.nBoardDto;
import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import com.gobit.minipj_gobit.noticeDept.repository.nBoardRepository;
import com.gobit.minipj_gobit.noticeDept.service.nBoardService;
import com.gobit.minipj_gobit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.PageFormat;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/noticeDept")
@RequiredArgsConstructor
public class nBoardController {

    @Autowired
    private nBoardService boardService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/list")
    public String NoticeList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<nBoard> paging = boardService.getNoticeList(page);
        model.addAttribute("postList", paging.getContent());
        return "/noticeDept/noticelist";
    }

    @GetMapping("/updateCnt/{id}")
    public String updateCnt(@PathVariable("id") Long id) {

        this.boardService.updateCnt(id);

        return "redirect:/noticeDept/detail/" + id;
    }


    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        nBoard board = this.boardService.getBoard(id);
        model.addAttribute("nBoard", board);
        return "noticeDept/noticeDetail";
    }

    @PostMapping("/noticeWrite")
    public String write(nBoard board,Principal principal) {
        User user = this.userRepository.findByUSERENO(Integer.parseInt(principal.getName())).get();
        boardService.write(board,user);
        return "redirect:/noticeDept/list";
    }

    @GetMapping("/modify/{id}")
    public String modify(Model model, @PathVariable("id") Long id) {
        nBoard board = boardService.getBoard(id);
        model.addAttribute("nBoard", board);
        return "noticeDept/noticeEdit";
    }

    @PostMapping("/modify/{id}")
    public String modifyCreate( @PathVariable("id") Long id,nBoard board) {
        boardService.modify(board, id);
        return "redirect:/noticeDept/detail/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        nBoard nBoard = this.boardService.getBoard(id);
        this.boardService.delete(nBoard);
        return "redirect:/noticeDept/list";
    }

    @GetMapping("/noticeWrite")
    public String noticeWrite() {
        return "/noticeDept/noticeWrite";
    }
}
