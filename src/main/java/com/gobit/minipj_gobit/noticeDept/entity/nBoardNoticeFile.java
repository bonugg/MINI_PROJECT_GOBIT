package com.gobit.minipj_gobit.noticeDept.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
