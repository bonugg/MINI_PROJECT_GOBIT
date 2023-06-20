package com.gobit.minipj_gobit.Entity;


import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_COMMUTE")
public class UserOnOff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMUTE_NUM")
    private long COMMUTENUM;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM")
    private User user;
    @Column(name = "GO_TO_WORK")
    private String START;
    @Column(name = "GET_OUT_WORK")
    private String END;
    @Column(name = "COMMUTE_TYPE")
    private String COMMUTETYPE;
    @Column(name = "COMMUTE_TIME")
    private int COMMUTETIME;
}
