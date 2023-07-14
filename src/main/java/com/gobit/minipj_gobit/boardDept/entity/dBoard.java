package com.gobit.minipj_gobit.boardDept.entity;

import com.gobit.minipj_gobit.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_BOARD_DEPT")
@Builder
public class dBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DBOARD_NUM")
    private Long id;

    @Column(name = "DOBARD_TITLE")
    private String title;

    @Lob
    @Column(name = "DBOARD_CONTENT", columnDefinition="LONGBLOB")
    private String content;

    @Column(name = "DBOARD_REGDATE")
    private LocalDateTime createDate;

    @Column(name = "DBOARD_UPDATEDATE")
    private LocalDateTime modifyDate;

    @Column(name = "DBOARD_CNT")
    private Integer cnt;

    @Column(name = "DBOARD_LIKE")
    private Integer like;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Reply> replyList;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<dBoardFile> files;
}
