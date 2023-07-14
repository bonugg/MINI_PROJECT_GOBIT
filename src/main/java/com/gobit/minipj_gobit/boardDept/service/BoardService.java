package com.gobit.minipj_gobit.boardDept.service;

import com.gobit.minipj_gobit.boardDept.dto.BoardDTO;
import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.repository.dBoardRepository;
import com.gobit.minipj_gobit.entity.User;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final dBoardRepository boardRepository;

    public Page<BoardDTO> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("modifyDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<dBoard> spec = search(kw);
        Page<dBoard> boardPage = boardRepository.findAll(spec, pageable);
        return change(boardPage);
    }

    public Page<BoardDTO> getListByCategory(int page, String category, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("modifyDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<dBoard> spec = searchByCategory(category, kw);
        Page<dBoard> boardPage = boardRepository.findAll(spec, pageable);
        return change(boardPage);
    }

    public Page<BoardDTO> getList(String dept, int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("modifyDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<dBoard> spec = search(kw);
        Page<dBoard> boardPage = boardRepository.findAllByUserUSERDEPT(dept, pageable);
        return change(boardPage);
    }

    public Page<BoardDTO> getListByCategory(String dept, int page, String category, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("modifyDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<dBoard> spec = searchByCategory(category, kw);
        Page<dBoard> boardPage = boardRepository.findAllByUserUSERDEPT(dept, pageable);
        return change(boardPage);
    }

    private Specification<dBoard> search(String kw) {
        return (b, query, cb) -> {
            query.distinct(true);
            Join<dBoard, User> u1 = b.join("user", JoinType.LEFT);
            String likeKeyword = "%" + kw + "%";
            return cb.or(
                    cb.like(b.get("title"), likeKeyword),
                    cb.like(b.get("content"), likeKeyword),
                    cb.like(u1.get("USERNAME"), likeKeyword)
            );
        };
    }

    private Specification<dBoard> searchByCategory(String category, String kw) {
        return (b, query, cb) -> {
            query.distinct(true);
            Join<dBoard, User> u1 = b.join("user", JoinType.LEFT);
            String likeKeyword = "%" + kw + "%";
            switch (category) {
                case "제목":
                    return cb.like(b.get("title"), likeKeyword);
                case "내용":
                    return cb.like(b.get("content"), likeKeyword);
                case "작성자":
                    return cb.like(u1.get("USERNAME"), likeKeyword);
                default:
                    return null;
            }
        };
    }

    public Page<BoardDTO> change(Page<dBoard> page) {
        return page.map(board -> BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .createDate(board.getCreateDate())
                .modifyDate(board.getModifyDate())
                .cnt(board.getCnt())
                .like(board.getLike())
                .username(board.getUser().getUSERNAME())
                .build()
        );
    }
}

