package com.gobit.minipj_gobit.noticeDept.controller;

import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.boardDept.entity.BoardForm;
import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.noticeDept.dto.nBoardDto;
import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import com.gobit.minipj_gobit.noticeDept.entity.nBoardNoticeFile;
import com.gobit.minipj_gobit.noticeDept.repository.nBoardRepository;
import com.gobit.minipj_gobit.noticeDept.service.NfileService;
import com.gobit.minipj_gobit.noticeDept.service.nBoardImgService;
import com.gobit.minipj_gobit.noticeDept.service.nBoardService;
import com.gobit.minipj_gobit.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.monitor.FileEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.PageFormat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/noticeDept")
@RequiredArgsConstructor
public class nBoardController {

    @Autowired
    private nBoardService boardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NfileService nfileService;

    @Autowired
    private nBoardImgService boardImgService;


    @GetMapping("/list")
    public String NoticeList(Model model,
                             @PageableDefault (page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                             String searchKeyword) {
        Page<nBoard> boardList = null;
        if(searchKeyword == null) {
            boardList = boardService.getNoticeList(pageable);
        }
        else {
            boardList = boardService.searchBoard(searchKeyword,searchKeyword, pageable);
        }

        int nowPage = boardList.getNumber() + 1;
        int startPage = Math.max(1, nowPage -5);
        int endPage = Math.min(nowPage + 5, boardList.getTotalPages());

        model.addAttribute("postList", boardList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
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
        List<nBoardNoticeFile> boardFileList = boardImgService.getBoardFiles(board);

        model.addAttribute("nBoard", board);
        model.addAttribute("boardFiles", boardFileList);
        return "noticeDept/noticeDetail";
    }

    @PostMapping("/noticeWrite")
    @Transactional
    public String write(nBoard board, Principal principal, MultipartFile[] files) {
        User user = this.userRepository.findByUSERENO(Integer.parseInt(principal.getName())).get();

        boardService.write(board, user);

        List<nBoardNoticeFile> boardNoticeFiles = nfileService.saveFiles(board, files);

       for(nBoardNoticeFile boardNoticeFile : boardNoticeFiles) {
           boardImgService.saveBoardFiles(boardNoticeFile);
       }

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
