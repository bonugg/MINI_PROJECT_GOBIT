package com.gobit.minipj_gobit.boardDept.service;

import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.entity.dBoardFile;
import com.gobit.minipj_gobit.boardDept.repository.dBoardFileRepository;
import com.gobit.minipj_gobit.boardDept.repository.dBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final dBoardFileRepository fileRepository;
    private final dBoardRepository boardRepository;

    @Transactional
    public void saveFiles(final Long boardId, final List<dBoardFile> files) {
        dBoard board = boardRepository.findById(boardId).get();
        for (dBoardFile file : files) {
            file.setBoard(board);
        }
        fileRepository.saveAll(files);
    }

    public List<Map<String, Object>> findAllFileByBoardId(Long boardId) {
        dBoard board = boardRepository.findById(boardId).get();
        List<dBoardFile> fileList = fileRepository.findAllByBoard(board);
        return changeListMap(fileList);
    }

    @Transactional
    public void deleteAllFile(dBoard board) {
        fileRepository.deleteAllByBoard(board);
    }

    public dBoardFile findById(Long id) {
        return fileRepository.findById(id).get();
    }

    List<Map<String, Object>> changeListMap(List<dBoardFile> fileList) {
        List<Map<String, Object>> returnListMap = fileList.stream().map(file -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", file.getId());
            map.put("originalName", file.getOriginalName());
            map.put("saveName", file.getSaveName());
            map.put("size", file.getSize());
            map.put("createDate", file.getCreateDate());
            return map;
        }).collect(Collectors.toList());

        return returnListMap;
    }
}
