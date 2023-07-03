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
    //결재번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APP_NUM")
    private long appNum;

    //결재종류: V 휴가, B 출장, M 회의. char(1)
    @NotNull
    @Column(name = "APP_SORT")
    private char appSort;

    //결재신청자
//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "USER_NUM")
    private User userNum;

    //사원ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_USER_NUM")
    private User user;

    //--------결제공통--------
    //결재 작성일
    @NotNull
    @Column(name = "APP_WRI_DATE")
    private LocalDateTime appWriDate = LocalDateTime.now();

    //결재상태: 미승인, 반려, 승인
    @NotNull
    @Column(name = "APP_STATE")
    private String appState;

    //결재상태확정날짜
    @Column(name = "APP_STATE_DATE")
    private LocalDateTime appStateDate = LocalDateTime.now();

    //결재승인자이름
    @JoinColumn(name = "USER_NAME")
    private String userName;

    //--------결제상세--------
    //결재시작일
    @NotNull
    @Column(name="APP_START")
    private LocalDateTime appStart;
    //결재종료일
    @NotNull
    @Column(name="APP_END")
    private LocalDateTime appEnd;
    //결재내용
    @NotNull
    @Column(name = "APP_CONTENT")
    private String appContent;
    //결재장소
    @Column(name="APP_LOCATION")
    private String appLocation;
    //회의참가자
    @Column(name="APP_PARTICIPANT")
    private String appParticipant;
    //휴가종류
    @Column(name="APP_VACTYPE")
    private String appVacType;
    //알림 전송용 코드
    @Column(name = "APP_ALARM")
    private int appAlarm;

    public ApprovalDTO toDTO(){
        char appSortChr = this.appSort;
        String appSortString = String.valueOf(appSortChr);
        ApprovalDTO approvalDTO = ApprovalDTO.builder()
                .appNum(this.appNum)
                .appSort(appSortString)
                .userNum(this.userNum)
                .appWriDate(this.appWriDate)
                .appState(this.appState)
                .userName(this.userName)
                .appStart(this.appStart)
                .appEnd(this.appEnd)
                .appContent(this.appContent)
                .appLocation(this.appLocation)
                .appParticipant(this.appParticipant)
                .appVacType(this.appVacType)
                .appAlarm(this.appAlarm)
                .build();
        return approvalDTO;
    }

}