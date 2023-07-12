package com.gobit.minipj_gobit.entity;

import com.gobit.minipj_gobit.dto.VacationDTO;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_vacation")
@Builder
public class Vacation {
    //휴가 식별지
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VAC_NUM")
    private long vacNum;
    //총연차
    @Column(name = "VAC_TOTAL")
    private long vacTotal;
    //사용일수
    @Column(name = "VAC_USED")
    private long vacUsed;
    //잔여일수
    @Column(name ="VAC_LEFT")
    private long vacLeft;
    //사원
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM")
    private User user;

    public VacationDTO toDTO(){
        // 초단위를 일(day) 단위로 변환
        long secondsPerDay = 24 * 60 * 60; // 초당 일(day) 수
        double vacTotalE = Math.round(((double) this.vacTotal / secondsPerDay) * 10) / 10.0;
        double vacUsedE = Math.round(((double) this.vacUsed / secondsPerDay) * 10) / 10.0;
        double vacLeftE = Math.round(((double) this.vacLeft / secondsPerDay) * 10) / 10.0;

        VacationDTO vacationDTO = VacationDTO.builder()
                .vacNum(this.vacNum)
                .vacTotalD(vacTotalE)
                .vacUsedD(vacUsedE)
                .vacLeftD(vacLeftE)
                .user(this.user)
                .build();
        return vacationDTO;
    }

    public void setDefault(User user){
        System.out.println("추가할 회원의 직급: " + user.getUSERPOSITION());
        if(user.getUSERPOSITION().equals("팀장")){
            this.vacTotal = 2160000;
            //25일
        }else{
            this.vacTotal = 1728000;
            //20일
        }
        this.vacLeft = this.vacTotal;
        this.vacUsed = 0;
        this.user = user;
    }

}
