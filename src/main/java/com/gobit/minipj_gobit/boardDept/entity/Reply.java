package com.gobit.minipj_gobit.boardDept.entity;

import com.gobit.minipj_gobit.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_BOARD_DEPT_REPLY")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLY_NUM")
    private Long id;

    @Column(name = "REPLY_CONTENT", length = 1000)
    private String content;

    @Column(name = "REPLY_REGDATE")
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "REPLY_UPDATEDATE")
    private LocalDateTime modifyDate;

    @ManyToOne
    private dBoard board;

    @ManyToOne
    private User user;
}
