package com.gobit.minipj_gobit.noticeDept.service;

import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.noticeDept.entity.nBoard;
import com.gobit.minipj_gobit.noticeDept.entity.nBoardFile;
import com.gobit.minipj_gobit.noticeDept.repository.NfileRepository;
import com.gobit.minipj_gobit.noticeDept.repository.nBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class nBoardService {
    private final nBoardRepository boardRepository;
    private final NfileService nfileService;
    private final NfileRepository nfileRepository;

//    @Autowired
//    public nBoardService(nBoardRepository boardRepository) {
//        this.boardRepository = boardRepository;
//    }

    public nBoard getBoard(Long id) {
        return boardRepository.findById(id).get();
    }


    public void updateCnt(Long id) {
        nBoard board = boardRepository.findById(id).get();
        int cnt = board.getCnt() + 1;
        board.setCnt(cnt);
        boardRepository.save(board);
    }

    public void delete(nBoard board) {
        List<nBoardFile> files = board.getFiles();
        boardRepository.delete(board);
        for (nBoardFile file : files) {
            nfileService.deleteFile(file);
        }
        nfileRepository.deleteAll(files);
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

    public Page<nBoard> getNoticeList(Pageable pageable) {

            return boardRepository.findAll(pageable);
    }


    public Long write(String title, String content, User user) {
        nBoard board = new nBoard();
        board.setTitle(title);
        board.setContent(content);
        board.setUser(user);

        boardRepository.save(board);

        return board.getId();
    }

    public void modify(Long id, String title, String content) {
        nBoard existingBoard = boardRepository.findById(id).get();
        existingBoard.setTitle(title);
        existingBoard.setContent(content);
        existingBoard.setUpdateDate(LocalDateTime.now());
        boardRepository.save(existingBoard);
    }

    public Page<nBoard> searchBoard(String searchKeyword, String keyword, Pageable pageable) {
        return boardRepository.findByContentContainingOrTitleContaining(searchKeyword, keyword,pageable);
    }
}
