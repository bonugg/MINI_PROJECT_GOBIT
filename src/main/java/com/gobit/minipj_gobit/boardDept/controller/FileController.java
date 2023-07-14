package com.gobit.minipj_gobit.boardDept.controller;

import com.gobit.minipj_gobit.boardDept.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class FileController {

    private final FileService fileService;
    @GetMapping("/file/files/{boardId}")
    public List<Map<String, Object>> findAllFileByBoardId(@PathVariable("boardId") Long boardId) {
        List<Map<String, Object>> files = fileService.findAllFileByBoardId(boardId);
        return files;
    }
}
