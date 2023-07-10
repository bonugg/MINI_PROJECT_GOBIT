package com.gobit.minipj_gobit.service.impl;

import com.gobit.minipj_gobit.entity.Vacation;
import com.gobit.minipj_gobit.repository.VacationRepository;
import com.gobit.minipj_gobit.service.VacationService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VacationServiceImpl implements VacationService {
    VacationRepository vacationRepository;
    public VacationServiceImpl(VacationRepository vacationRepository){
        this.vacationRepository = vacationRepository;
    }

    @Override
    public Vacation getVacation(long userNum) {
        if(vacationRepository.findByUserNum(userNum) == null){
            return null;
        }
        return vacationRepository.findByUserNum(userNum);
    }

    public long getVacTotal(long userNum){
        long total = vacationRepository.findVacTotalByUserNum(userNum);
        return total;
    }

    @Override
    public long getVacLeft(long userNum) {
        Long left = vacationRepository.findVacLeftByUserNum(userNum);
        return left != null ? left : 0;
    }

    @Override
    public long getVacUsed(long userNum) {
        long used = vacationRepository.findVacUsedByUserNum(userNum);
        return used;
    }

    @Modifying
    @Transactional
    @Override
    public void saveVacation(long vacUsed, long vacLeft, long userNum) {
        vacationRepository.request(vacUsed, vacLeft, userNum);
    }


    @Override
    public void updateVacation(long requestSecond) {

    }
}
