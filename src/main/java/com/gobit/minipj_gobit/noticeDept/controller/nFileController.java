package com.gobit.minipj_gobit.noticeDept.controller;

import com.gobit.minipj_gobit.boardDept.service.FileService;
import com.gobit.minipj_gobit.noticeDept.service.NfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class nFileController {

    private final NfileService fileService;
    @GetMapping("/nfile/files/{boardId}")
    public List<Map<String, Object>> findAllFileByBoardId(@PathVariable("boardId") Long boardId) {
        List<Map<String, Object>> files = fileService.findAllFileByBoardId(boardId);
        return files;
    }
}
