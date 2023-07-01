package com.gobit.minipj_gobit.boardDept.controller;

import com.gobit.minipj_gobit.boardDept.entity.dBoardFile;
import com.gobit.minipj_gobit.boardDept.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    // 파일 리스트 조회
    @GetMapping("/board/{boardId}/files")
    public List<dBoardFile> findAllFileByBoardId(@PathVariable final Long boardId) {
        return fileService.findAllFileByPostId(boardId);
    }
}
