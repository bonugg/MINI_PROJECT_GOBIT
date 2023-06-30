package com.gobit.minipj_gobit.boardDept.service;

import com.gobit.minipj_gobit.boardDept.entity.Reply;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.boardDept.entity.Like;
import com.gobit.minipj_gobit.boardDept.entity.dBoard;
import com.gobit.minipj_gobit.boardDept.entity.dBoardFile;
import com.gobit.minipj_gobit.boardDept.repository.LikeRepository;
import com.gobit.minipj_gobit.boardDept.repository.dBoardFileRepository;
import com.gobit.minipj_gobit.boardDept.repository.dBoardRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class dBoardService {
    private final dBoardRepository dBoardRepository;
    private final LikeRepository likeRepository;
    private final dBoardFileRepository boardFileRepository;

    public Page<dBoard> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("modifyDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<dBoard> spec = search(kw);
        return this.dBoardRepository.findAll(spec, pageable);
    }

    public dBoard getBoard(Long id) {
        return this.dBoardRepository.findById(id).get();
    }

    public void updateCnt(Long id) {
        dBoard board = this.dBoardRepository.findById(id).get();
        int cnt = board.getCnt() + 1;
        board.setCnt(cnt);
        this.dBoardRepository.save(board);
    }

    public void create(dBoard board, List<dBoardFile> fileList) {
        dBoardRepository.save(board);
        dBoardRepository.flush();
        for (dBoardFile file : fileList) {
            file.setBoard(board);
            boardFileRepository.save(file);
        }
    }

    public void modify(dBoard board, String title, String content) {
        board.setTitle(title);
        board.setContent(content);
        board.setCreateDate(board.getCreateDate());
        board.setModifyDate(LocalDateTime.now());
        this.dBoardRepository.save(board);
    }

    public void delete(dBoard board) {
        this.dBoardRepository.delete(board);
    }


    public void like(dBoard board, User user) {
        long bId = board.getId();
        long uId = user.getUSERENO();

        if (this.likeRepository.findByBoardIdAndUserId(bId, uId) == null) {
            Like like = new Like();

            like.setBoardId(bId);
            like.setUserId(uId);
            this.likeRepository.save(like);
            board.setLike(this.likeRepository.countByBoardId(bId));
            this.dBoardRepository.save(board);
        }
    }

    private Specification<dBoard> search(String kw) {
        return new Specification<dBoard>() {
            @Override
            public Predicate toPredicate(Root<dBoard> b, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                Join<dBoard, User> u1 = b.join("user", JoinType.LEFT);
                Join<dBoard, Reply> a = b.join("replyList", JoinType.LEFT);
                Join<Reply, User> u2 = a.join("user", JoinType.LEFT);
                return cb.or(cb.like(b.get("title"), "%" + kw + "%"), // 제목
                        cb.like(b.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("USERNAME"), "%" + kw + "%"),    // 게시글 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 댓글 내용
                        cb.like(u2.get("USERNAME"), "%" + kw + "%"));   // 댓글 작성자
            }
        };
    }
}
