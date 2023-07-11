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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class dBoardService {
    private final dBoardRepository dBoardRepository;
    private final LikeRepository likeRepository;
    private final dBoardFileRepository boardFileRepository;
    private final FileService fileService;

    public Page<dBoard> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("modifyDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<dBoard> spec = search(kw);
        return this.dBoardRepository.findAll(spec, pageable);
    }

    public Page<dBoard> getListByCategory(int page, String category, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("modifyDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<dBoard> spec = searchByCategory(category, kw);
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

    public Long create(String title, String content, User user) {
        dBoard board = dBoard.builder()
                .title(title)
                .content(content)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .cnt(0)
                .like(0)
                .user(user)
                .build();
        dBoardRepository.save(board);
        return board.getId();
    }

    public void modify(Long id, String title, String content) {
        dBoard modifyBoard = dBoardRepository.findById(id).get();
        modifyBoard.setTitle(title);
        modifyBoard.setContent(content);
        modifyBoard.setModifyDate(LocalDateTime.now());
        this.dBoardRepository.save(modifyBoard);
    }

    public void delete(dBoard board) {
        List<dBoardFile> fileList = boardFileRepository.findAllByBoard(board);

        for (dBoardFile file : fileList) {
            fileService.deleteFile(file);
        }
        boardFileRepository.deleteAll(fileList);
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

    private Specification<dBoard> searchByCategory(String category, String kw) {
        return (b, query, cb) -> {
            query.distinct(true);
            Join<dBoard, User> u1 = b.join("user", JoinType.LEFT);
            Join<dBoard, Reply> a = b.join("replyList", JoinType.LEFT);
            switch (category) {
                case "제목":
                    return cb.like(b.get("title"), "%" + kw + "%");
                case "내용":
                    return cb.like(b.get("content"), "%" + kw + "%");
                case "작성자":
                    return cb.like(u1.get("USERNAME"), "%" + kw + "%");
            }
            // 기본적으로 null을 반환하거나 다른 조건을 추가하여 반환할 수 있습니다.
            return null;
        };
    }

    public Page<dBoard> getAllPostsSortedByCnt(Pageable pageable) {
        return dBoardRepository.findAllByOrderByCntDesc(pageable);
    }

    public Page<dBoard> getAllPostsSortedByLike(Pageable pageable) {
        return dBoardRepository.findAllByOrderByLikeDesc(pageable);
    }

}
