package com.gobit.minipj_gobit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gobit.minipj_gobit.entity.AppMeeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppMeetingDTO {
    private Long metNum;
    private long appNum;
    private String metContent;
    private String metLocation;
    private String metParticipant;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate metEnd;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate metStart;

    public AppMeeting toEntity(){
        AppMeeting appMeeting = AppMeeting.builder()
                .metNum(this.metNum)
                .appNum(this.appNum)
                .metContent(this.metContent)
                .metLocation(this.metLocation)
                .metParticipant(this.metParticipant)
                .metEnd(this.metEnd)
                .metStart(this.metStart)
                .build();
        return appMeeting;
    }

}