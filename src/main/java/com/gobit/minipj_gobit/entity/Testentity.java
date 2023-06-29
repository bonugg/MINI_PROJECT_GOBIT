package com.gobit.minipj_gobit.entity;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(TestentityListener.class)
@Table(name = "T_TEST")
public class Testentity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEST_NUM")
    private long testNum;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "USER_NUM")
    @NotNull
    private User user;
    @Column(name = "TEST_ONOFF")
    private String check;

    @Override
    public String toString() {
        return "Testentity{" +
                "testNum=" + testNum +
                ", check='" + check + '\'' +
                '}';
    }
}
