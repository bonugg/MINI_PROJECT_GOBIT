package com.gobit.minipj_gobit.entity;

import com.gobit.minipj_gobit.dto.AppMeetingDTO;
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
@Table(name = "T_MEETING")
//@SequenceGenerator(
//        name="AppMeetSeqGenerator",
//        sequenceName = "T_MEET_SEQ",
//        initialValue = 3,
//        allocationSize = 1
//)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppMeeting {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
//            generator = "AppMeetSeqGenerator"
    )
    @Column(name="MET_NUM")
    private Long metNum;

    @NotNull
    @Column(name="APP_NUM")
    private long appNum;

    @Column(name="MET_CONTENT")
    private String metContent;

    @NotNull
    @Column(name="MET_LOCATION")
    private String metLocation;

    @NotNull
    @Column(name="MET_PARTICIPANT")
    private String metParticipant;

    @NotNull
    @Column(name="MET_END")
    private LocalDate metEnd;

    @NotNull
    @Column(name="MET_START")
    private LocalDate metStart;

    @PrePersist
    public void prePersistMetNum(){
        int sequenceNum = 1;
        if(this.metNum == null) {
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            this.metNum = Long.parseLong("3" + currentDate + sequenceNum++);
        }
    }

    public AppMeetingDTO toDTO(){
        AppMeetingDTO appMeetingDTO = AppMeetingDTO.builder()
                .metNum(this.metNum)
                .appNum(this.appNum)
                .metContent(this.metContent)
                .metLocation(this.metLocation)
                .metParticipant(this.metParticipant)
                .metEnd(this.metEnd)
                .metStart(this.metStart)
                .build();
        return appMeetingDTO;
    }

}