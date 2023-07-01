package com.gobit.minipj_gobit.entity;

import com.gobit.minipj_gobit.dto.ApprovalDTO;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "T_APPROVAL")
public class Approval {

    //--------결제식별--------
    //결재문서ID. auto_increase
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APP_NUM")
    private long appNum;

    //임의 값 설정함 V 휴가, T 출장, M 회의. char(1)
    @NotNull
    @Column(name = "APP_SORT", nullable = false)
    private char appSort;

    //사원ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_USER_NUM")
    private User user;

    //--------결제공통--------
    //결재 작성일
    @Column(name = "APP_WRI_DATE")
    private LocalDateTime appWriDate = LocalDateTime.now();

    // 결재상태. 영문자 코드로 해서 char(1)로 하는건?
    @Column(name = "APP_STATE", nullable = false)
    private String appState;

    @Column(name = "APP_STATE_DATE")
    private LocalDateTime appStateDate = LocalDateTime.now();


    //--------결제상세--------
    //내용
    @Column(name = "APP_CONTENT", nullable = false)
    private String appContent;
    //장소
    @Column(name="APP_LOCATION")
    private String appLocation;
    //시작일
    @NotNull
    @Column(name="APP_START")
    private LocalDateTime appStart;
    //종료일
    @NotNull
    @Column(name="APP_END")
    private LocalDateTime appEnd;
    //회의참가자
    @Column(name="APP_PARTICIPANT")
    private String appParticipant;
    //휴가종류
    @Column(name="APP_VACTYPE")
    private String appVacType;
    //알림 전송용 코드
    @Column(name = "APP_ALARM", columnDefinition = "int default 0")
    private int appAlarm;

}