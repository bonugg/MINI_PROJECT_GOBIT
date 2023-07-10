package com.gobit.minipj_gobit.entity;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_CALENDAR")
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAL_NUM")
    private long CALNUM;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM")
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ONOFF")
    private UserOnOff userOnOff;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_APPROVAL")
    private Approval approval;
    @NotNull
    @Column(name = "CAL_TITLE")
    private String CALTITLE;
    @NotNull
    @Column(name = "CAL_START")
    private String CALSTART;
    @NotNull
    @Column(name = "CAL_END")
    private String CALEND;
    @NotNull
    @Column(name = "CAL_CONTENT")
    private String CALCONTENT;
    @NotNull
    @Column(name = "CAL_TYPE")
    private String CALTYPE;

    @Override
    public String toString() {
        return "Calendar{" +
                "CALNUM=" + CALNUM +
                ", CALTITLE='" + CALTITLE + '\'' +
                ", CALSTART='" + CALSTART + '\'' +
                ", CALEND='" + CALEND + '\'' +
                ", CALCONTENT='" + CALCONTENT + '\'' +
                ", CALTYPE='" + CALTYPE + '\'' +
                '}';
    }
}
