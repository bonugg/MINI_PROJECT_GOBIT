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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalDTO {
    //--------결제식별--------
    private long appNum;                //결재번호
    private String appSort;             //결재종류: V/M/B
    private User userNum;               //결재신청자
    //--------결제공통--------
    private LocalDateTime appWriDate;   //결재작성일
    private String appState;            //결재상태
    private LocalDateTime appStateDate; //결재상태확정날짜
    private String userName;            //결재승인자이름

    //--------결제상세--------
    private LocalDateTime appStart;     //결재_시작일
    private LocalDateTime appEnd;       //결재_종료일
    private String appContent;          //결재내용
    private String appLocation;         //결재_장소
    private String appParticipant;      //결재_회의참가자
    private String appVacType;          //결재_휴가종류
    private int appAlarm;               //알림 전송용 코드


    public Approval toEntity(){
        String appSortString = this.appSort;
        char appSortChr = appSortString.charAt(0);
        Approval approval = Approval.builder()
                .appNum(this.appNum)
                .appSort(appSortChr)
                .userNum(this.userNum)
                .appWriDate(LocalDateTime.now())
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
        return approval;
    }
}