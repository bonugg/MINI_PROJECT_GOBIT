package com.gobit.minipj_gobit.boardDept.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class dBoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalName;
    private String saveName;
    private String type;
    private Long size;
    @ManyToOne
    private dBoard board;

}
