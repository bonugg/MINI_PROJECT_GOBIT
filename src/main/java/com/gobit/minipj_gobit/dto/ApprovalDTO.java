package com.gobit.minipj_gobit.dto;

import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalDTO {
    //--------결제식별--------
    private long appNum;                //결재번호
    private String appSort;             //결재종류: V/M/B
    private User userNum;               //결재신청자Id
    private User appUserNum;            //결재승인자Id
    //--------결제공통--------
    private LocalDateTime appWriDate;   //결재작성일
    private String appState;            //결재상태
    private LocalDateTime appStateDate; //결재상태확정날짜
    private String userName;            //결재승인자이름
    private String userDept;            //결재신청자부서

    //--------결제상세--------
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime appStart;     //결재_시작일
    private LocalDateTime appEnd;       //결재_종료일
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appStart2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appEnd2;
    private String appContent;          //결재내용
    private String appLocation;         //결재_장소
    private String appParticipant;      //결재_회의참가자
    private String appVacType;          //결재_휴가종류
    private long appVacReq;             //결재_휴가신청일
    private double appVacReqDaysD;
    private int appAlarm;               //알림 전송용 코드


    public Approval toEntity() {
        String appSortString = this.appSort;
        String appSortChr = appSortString;

        Approval.ApprovalBuilder builder = Approval.builder()
                .appNum(this.appNum)
                .appSort(appSortChr)
                .userNum(this.userNum)
                .appUserNum(this.appUserNum)
                .appWriDate(LocalDateTime.now())
                .appState(this.appState)
                .userName(this.userName)
                .userDept(this.userDept)
                .appContent(this.appContent)
                .appLocation(this.appLocation)
                .appParticipant(this.appParticipant)
                .appVacType(this.appVacType)
                .appVacReq(this.appVacReq)
                .appAlarm(this.appAlarm);

        if (this.appStart == null || this.appEnd == null) {
            LocalDateTime appStartDateTime = LocalDateTime.of(appStart2, LocalTime.MIDNIGHT);
            LocalDateTime appEndDateTime = LocalDateTime.of(appEnd2, LocalTime.MIDNIGHT);
            builder.appStart(appStartDateTime)
                    .appEnd(appEndDateTime);
        } else {
            builder.appStart(this.appStart)
                    .appEnd(this.appEnd);
        }

        return builder.build();
    }
}