package com.gobit.minipj_gobit.boardDept.service;

import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.entity.dBoardFile;
import com.gobit.minipj_gobit.boardDept.repository.dBoardFileRepository;
import com.gobit.minipj_gobit.boardDept.repository.dBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
@Service
@RequiredArgsConstructor
public class FileService {
    private final dBoardFileRepository fileRepository;
    private final dBoardRepository boardRepository;

    @Transactional
    public void saveFiles(final Long boardId, final List<dBoardFile> files) {
        dBoard board = boardRepository.findById(boardId).get();
        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        for (dBoardFile file : files) {
            file.setBoard(board);
        }
        fileRepository.saveAll(files);
    }
    /**
     * 파일 리스트 조회
     * @param postId - 게시글 번호 (FK)
     * @return 파일 리스트
     */
    public List<dBoardFile> findAllFileByPostId(final Long postId) {
        return fileRepository.findAllByBoardId(postId);
    }

    /**
     * 파일 리스트 조회
     * @param ids - PK 리스트
     * @return 파일 리스트
     */
    public List<dBoardFile> findAllFileByIds(final List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return fileRepository.findAllByIdIn(ids);
    }

    /**
     * 파일 삭제 (from Database)
     * @param ids - PK 리스트
     */
    @Transactional
    public void deleteAllFileByIds(final List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        fileRepository.deleteAllByIdIn(ids);
    }

    public dBoardFile findById(Long id) {
        return fileRepository.findById(id).get();
    }
}
