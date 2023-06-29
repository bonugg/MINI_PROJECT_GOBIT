package com.gobit.minipj_gobit.entity;


import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(UserOnOffEntityListener.class)
@Table(name = "T_COMMUTE")
public class UserOnOff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMUTE_NUM")
    private long COMMUTENUM;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "USER_NUM")
    @NotNull
    private User user;
    @Column(name = "GO_TO_WORK")
    private String START;
    @Column(name = "GET_OUT_WORK")
    private String END;
    @Column(name = "COMMUTE_TYPE")
    private String COMMUTETYPE;
    @Column(name = "COMMUTE_TIME")
    private double COMMUTETIME;
    @Column(name = "UPDATE_AT")
    @UpdateTimestamp
    private LocalDateTime UPDATEAT;

    @Override
    public String toString() {
        return "UserOnOff{" +
                "COMMUTENUM=" + COMMUTENUM +
                ", START='" + START + '\'' +
                ", END='" + END + '\'' +
                ", COMMUTETYPE='" + COMMUTETYPE + '\'' +
                ", COMMUTETIME=" + COMMUTETIME +
                ", UPDATEAT=" + UPDATEAT +
                '}';
    }
}
