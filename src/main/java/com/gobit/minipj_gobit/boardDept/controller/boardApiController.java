package com.gobit.minipj_gobit.boardDept.controller;

import com.gobit.minipj_gobit.boardDept.controller.dto.BoardDTO;
import com.gobit.minipj_gobit.boardDept.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class boardApiController {

    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<Page<BoardDTO>> getBoardDeptList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "전체") String category,
            @RequestParam(defaultValue = "") String kw) {
        Page<BoardDTO> boardDeptPage;

        if (category.equals("전체")) {
            boardDeptPage = boardService.getList(page, kw);
        } else {
            boardDeptPage = boardService.getListByCategory(page, category, kw);
        }

        return ResponseEntity.ok(boardDeptPage);
    }

    @GetMapping("/list/dept")
    public ResponseEntity<Page<BoardDTO>> getBoardDeptList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "전체") String category,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "IT") String dept) {
        Page<BoardDTO> boardDeptPage;

        if (category.equals("전체")) {
            boardDeptPage = boardService.getList(dept, page, kw);
        } else {
            boardDeptPage = boardService.getListByCategory(dept, page, category, kw);
        }

        return ResponseEntity.ok(boardDeptPage);
    }

}
