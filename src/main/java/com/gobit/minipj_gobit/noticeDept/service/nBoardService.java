package com.gobit.minipj_gobit.noticeDept.service;

import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.noticeDept.dto.nBoardDto;
import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import com.gobit.minipj_gobit.noticeDept.repository.nBoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class nBoardService {
    private nBoardRepository boardRepository;

    @Autowired
    public nBoardService(nBoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public nBoard getBoard(Long id) {
        return boardRepository.findById(id).get();
    }


    public void updateCnt(Long id) {
        nBoard board = boardRepository.findById(id).get();
        int cnt = board.getCnt() + 1;
        board.setCnt(cnt);
        boardRepository.save(board);
    }

    public void delete(nBoard nBoard) {
        boardRepository.delete(nBoard);
    }

//    public List<nBoardDto> getNoticeList() {
//        List<nBoard> boardlist = nBoardRepository.findAll();
//        List<nBoardDto> nBoardDtoList = new ArrayList<>();
//
//        for (nBoard board : boardlist) {
//            nBoardDto boardDto = nBoardDto.builder()
//                    .id(board.getId())
//                    .title(board.getTitle())
//                    .content(board.getContent())
//                    .regDate(board.getRegDate())
//                    .updateDate(board.getUpdateDate())
//                    .cnt(board.getCnt())
//                    .build();
//            nBoardDtoList.add(boardDto);
//
//        }
//        return nBoardDtoList;
//
//    }

    public Page<nBoard> getNoticeList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("updateDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return boardRepository.findAll(pageable);
    }


    public nBoard write(nBoard board,User user) {

        board.setUser(user);

        return boardRepository.save(board);
    }

    public nBoard modify(nBoard board, Long id) {
        nBoard existingBoard = boardRepository.findById(id).get();
        existingBoard.setTitle(board.getTitle());
        existingBoard.setContent(board.getContent());

        return boardRepository.save(existingBoard);
    }
}
