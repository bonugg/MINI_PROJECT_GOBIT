package com.gobit.minipj_gobit.boardDept.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class dBoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalName;
    private String saveName;
    private Long size;
    private LocalDateTime createDate;
    @ManyToOne
    private dBoard board;

    @Builder
    public dBoardFile(String originalName, String saveName, long size) {
        this.originalName = originalName;
        this.saveName = saveName;
        this.size = size;
        this.createDate = LocalDateTime.now();
    }
}
