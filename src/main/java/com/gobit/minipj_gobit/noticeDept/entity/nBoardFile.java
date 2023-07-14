package com.gobit.minipj_gobit.noticeDept.entity;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "T_BOARD_NOTICE_FILE")
@NoArgsConstructor
public class nBoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalName;
    private String saveName;
    private Long size;
    private LocalDateTime createDate;
    @ManyToOne
    @JoinColumn(name = "NBOARD_NUM")
    private nBoard board;
    @Builder
    public nBoardFile(String originalName, String saveName, long size) {
        this.originalName = originalName;
        this.saveName = saveName;
        this.size = size;
        this.createDate = LocalDateTime.now();
    }
}
