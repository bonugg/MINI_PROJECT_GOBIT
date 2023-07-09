package com.gobit.minipj_gobit.noticeDept.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_BOARD_NOTICE_FILE")
public class nBoardNoticeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NFILE_NUM")
    private Long nfileNo;

    @Column(name = "NFILE_NAME")
    private String nfileName;

    @Column(name = "NFILE_PATH")
    private String nfilePath;

    @Column(name = "NFILE_ORIGIN")
    private String nfileOrigin;

    @ManyToOne
    @JoinColumn(name = "NBOARD_NUM")
    private nBoard board;

    @Builder
    public nBoardNoticeFile(nBoard board, Long nfileNo, String nfileName, String nfileOrigin, String nfilePath) {
        this.board = board;
        this.nfileNo = nfileNo;
        this.nfileName = nfileName;
        this.nfileOrigin = nfileOrigin;
        this.nfilePath = nfilePath;
    }

}
