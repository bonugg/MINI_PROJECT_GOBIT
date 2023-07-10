package com.gobit.minipj_gobit.dto;

import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.entity.Vacation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacationDTO {

    private long vacNum;    //휴가 식별자
    private long vacTotal;  //휴가 총 연차
    private long vacUsed;   //휴가 사용일수
    private long vacLeft;   //휴가 잔여일수
    private User user;      //사원

    public Vacation toEntity(){
        Vacation vacation = Vacation.builder()
                .vacNum(this.vacNum)
                .vacTotal(this.vacTotal)
                .vacUsed(this.vacUsed)
                .vacLeft(this.vacLeft)
                .user(this.user)
                .build();
        return  vacation;
    }
}
