package com.gobit.minipj_gobit.boardDept.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "T_DBOARD_LIKE")
@Data
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long boardId;
    private Long userId;
}
