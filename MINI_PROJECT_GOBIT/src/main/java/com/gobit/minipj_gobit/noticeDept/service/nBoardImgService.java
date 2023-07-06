package com.gobit.minipj_gobit.noticeDept.service;

import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import com.gobit.minipj_gobit.noticeDept.entity.nBoardNoticeFile;
import com.gobit.minipj_gobit.noticeDept.repository.NfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class nBoardImgService {

    @Autowired
    private NfileRepository nfileRepository;

    public nBoardNoticeFile saveBoardFiles(nBoardNoticeFile boardfiles) {
        return nfileRepository.save(boardfiles);
    }

    public List<nBoardNoticeFile> getBoardFiles(nBoard board) {
        return nfileRepository.findByBoard(board);     // 상세게시글 이미지 불러오기용
    }
}
