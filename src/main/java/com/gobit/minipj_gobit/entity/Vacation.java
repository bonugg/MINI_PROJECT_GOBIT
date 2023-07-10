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
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "USERNUM")
    private User user;

//    @Builder
//    public Vacation(User user){
//        this.user = user;
//        if(user.getUSERPOSITION().equals("팀장")){
//            this.vacTotal = 25;
//        }else{
//            this.vacTotal = 20;
//        }
//    }

    public void setDefaultVacTotal(){
        if(user.getUSERPOSITION().equals("팀장")){
            this.vacTotal = 2160000;
            //25일
        }else{
            this.vacTotal = 1728000;
            //20일
        }
    }

    public void setInitialVacLeft(){
        this.vacLeft = this.vacTotal;
    }


    public VacationDTO toDTO(){
        VacationDTO vacationDTO = VacationDTO.builder()
                .vacNum(this.vacNum)
                .vacTotal(this.vacTotal)
                .vacUsed(this.vacUsed)
                .vacLeft(this.vacLeft)
                .user(this.user)
                .build();
        return vacationDTO;
    }

}
