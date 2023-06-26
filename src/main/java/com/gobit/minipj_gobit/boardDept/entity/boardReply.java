package com.gobit.minipj_gobit.boardDept.entity;

import com.gobit.minipj_gobit.Entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
//@Entity
@Table(name = "T_BOARD_DEPT_REPLY")
public class boardReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLY_NUM")
    private Long id;

    @Column(name = "REPLY_CONTENT")
    private String content;

    @Column(name = "REPLY_REGDATE")
    private LocalDateTime createDate;

    @Column(name = "REPLY_UPDATEDATE")
    private LocalDateTime modifyDate;

    @ManyToOne
    @Column(name = "DBOARD_NUM")
    private dBoard board;

//    @ManyToOne
//    private User user;
}
