package com.gobit.minipj_gobit.dto;

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
    private long appNum;
    private char appSort;
    private User user;
    private LocalDateTime appWriDate;
    private LocalDateTime appState;
    private long appUserNum;
    private String appContent;
    private String appLocation;
    private LocalDateTime appStart;
    private LocalDateTime appEnd;
    private String appParticipant;
    private String appVacType;


}