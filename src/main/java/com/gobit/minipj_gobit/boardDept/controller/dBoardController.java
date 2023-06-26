package com.gobit.minipj_gobit.boardDept.controller;

import com.gobit.minipj_gobit.Entity.User;
import com.gobit.minipj_gobit.boardDept.entity.BoardForm;
import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.service.dBoardService;
import com.gobit.minipj_gobit.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/boardDept")
@RequiredArgsConstructor
public class dBoardController {

    private final dBoardService dBoardService;
    private final UserRepository userRepository;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<dBoard> paging = this.dBoardService.getList(page);
        model.addAttribute("paging", paging);
        return "boardDept/dboardListPage";
    }

    @GetMapping("/updateCnt/{id}")
    public String updateCnt(@PathVariable("id") Long id) {

        this.dBoardService.updateCnt(id);

        return "redirect:/boardDept/detail/" + id;
    }


    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        dBoard dBoard = this.dBoardService.getBoard(id);
        model.addAttribute("board", dBoard);
        return "boardDept/dboardDetailPage";
    }

    @GetMapping("/create")
    public String create(BoardForm boardForm) {
        return "boardDept/dboardWritePage";
    }

    @PostMapping("/create")
    public String create(dBoard board, Principal principal) {
        User user = this.userRepository.findByUSERENO(Integer.parseInt(principal.getName())).get();
        this.dBoardService.create(board, user);
        return "redirect:/boardDept/list";
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, BoardForm boardForm) {
        dBoard board = this.dBoardService.getBoard(id);

        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());

        return "boardDept/dboardWritePage";
    }

    @PostMapping("/modify/{id}")
    public String modifyCreate(@PathVariable("id") Long id, BoardForm boardForm) {
        dBoard board = this.dBoardService.getBoard(id);
        this.dBoardService.modify(board, boardForm.getTitle(), boardForm.getContent());
        return "redirect:/boardDept/detail/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        dBoard board = this.dBoardService.getBoard(id);
        this.dBoardService.delete(board);
        return "redirect:/boardDept/list";
    }


}
