package com.gobit.minipj_gobit.noticeDept.service;

import com.gobit.minipj_gobit.boardDept.entity.dBoardFile;
import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import com.gobit.minipj_gobit.noticeDept.entity.nBoardFile;
import com.gobit.minipj_gobit.noticeDept.repository.NfileRepository;
import com.gobit.minipj_gobit.noticeDept.repository.nBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.gobit.minipj_gobit.boardDept.file.FileUtils.UPLOAD_PATH;

@Service
@RequiredArgsConstructor
public class NfileService {
    private final NfileRepository fileRepository;
    private final nBoardRepository boardRepository;

    @Transactional
    public void saveFiles(final Long boardId, final List<nBoardFile> files) {
        nBoard board = boardRepository.findById(boardId).get();
        for (nBoardFile file : files) {
            file.setBoard(board);
        }
        fileRepository.saveAll(files);
    }

    public List<Map<String, Object>> findAllFileByBoardId(Long boardId) {
        nBoard board = boardRepository.findById(boardId).get();
        List<nBoardFile> fileList = fileRepository.findAllByBoard(board);
        return changeListMap(fileList);
    }

    public nBoardFile findById(Long id) {
        return fileRepository.findById(id).get();
    }

    List<Map<String, Object>> changeListMap(List<nBoardFile> fileList) {
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

    public List<nBoardFile> findByFiles(Long boardId) {
        nBoard board = boardRepository.findById(boardId).get();
        List<nBoardFile> fileList = fileRepository.findAllByBoard(board);
        return fileList;
    }

    public void modifyFiles(List<String> existingFiles, List<nBoardFile> modifiedFiles) {
        List<nBoardFile> newFiles = new ArrayList<>();

        for (nBoardFile file : modifiedFiles) {
            boolean isExisting = false;

            for (String str : existingFiles) {
                if (str.equals(file.getOriginalName())) {
                    isExisting = true;
                    break;
                }
            }

            if (!isExisting) {
                newFiles.add(file);
            }
        }

        for (nBoardFile file : newFiles) {
            deleteFile(file);
            fileRepository.delete(file);
        }
    }

    public void deleteFile(nBoardFile file) {
        String uploadedDate = file.getCreateDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String filename = file.getSaveName();
        Path filePath = Paths.get(UPLOAD_PATH, uploadedDate, filename);

        try {
            // 파일 삭제
            Files.deleteIfExists(filePath);

            // 파일이 삭제되었는지 확인
            if (Files.exists(filePath)) {
                System.out.println("파일 삭제에 실패하였습니다.");
            } else {
                System.out.println("파일이 성공적으로 삭제되었습니다.");
            }
        } catch (Exception e) {
            System.out.println("파일 삭제 중 오류가 발생하였습니다: " + e.getMessage());
        }
    }

}
