package com.gobit.minipj_gobit.boardDept.entity;

import com.gobit.minipj_gobit.Entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_BOARD_DEPT")
public class dBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DBOARD_NUM")
    private Long id;

    @Column(name = "DOBARD_TITLE")
    private String title;

    @Column(name = "DBOARD_CONTENT")
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
}
