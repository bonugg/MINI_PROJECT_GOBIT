package com.gobit.minipj_gobit.entity;

import com.gobit.minipj_gobit.dto.ApprovalDTO;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;
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
@EntityListeners(ApprovalListener.class)
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
    private String appSort;

    //결재신청자
//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "USER_NUM")
    private User userNum;

    //결재승인자ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_USER_NUM")
    private User appUserNum;

    //--------결제공통--------
    //결재 작성일
    @NotNull
    @Column(name = "APP_WRI_DATE")
    private LocalDateTime appWriDate = LocalDateTime.now();

    //결재상태: 미승인, 반려, 승인
    @NotNull
    @Column(name = "APP_STATE")
    private String appState = "미승인";

    //결재상태확정날짜
    @Column(name = "APP_STATE_DATE")
    private LocalDateTime appStateDate = LocalDateTime.now();

    //결재승인자이름
    @JoinColumn(name = "USER_NAME")
    private String userName;

    //결재승인자부서
    @JoinColumn(name="USER_DEPT")
    private String userDept;

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
    //휴가 신청일 임시저장
    @Nullable
    @Column(name = "APP_VACREQ")
    private long appVacReq;
    //알림 전송용 코드
    @Column(name = "APP_ALARM")
    private int appAlarm;

    @Column(name = "APP_SIGN", length = 50000)
    private String appSign;

    @Column(name = "APP_CANCLE_REASON", length = 50000)
    private String appCancleReason;

    public ApprovalDTO toDTO(){
        // 초단위를 일(day) 단위로 변환
        long secondsPerDay = 24 * 60 * 60; // 초당 일(day) 수
        double appVacReqDaysE = (double) this.appVacReq / secondsPerDay;

        ApprovalDTO approvalDTO = ApprovalDTO.builder()
                .appNum(this.appNum)
                .appSort(this.appSort)
                .userNum(this.userNum)
                .appUserNum(this.appUserNum)
                .appWriDate(this.appWriDate)
                .appState(this.appState)
                .userName(this.userName)
                .userDept(this.userDept)
                .appStart(this.appStart)
                .appEnd(this.appEnd)
                .appContent(this.appContent)
                .appLocation(this.appLocation)
                .appParticipant(this.appParticipant)
                .appVacType(this.appVacType)
                .appVacReqDaysD(appVacReqDaysE)
                .appAlarm(this.appAlarm)
                .appSign(this.appSign)
                .build();
        return approvalDTO;
    }

}