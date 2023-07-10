package com.gobit.minipj_gobit.service;

import com.gobit.minipj_gobit.entity.Vacation;

public interface VacationService {
//    void updateVacation(LocalDateTime appStart, LocalDateTime appEnd);

    Vacation getVacation(long userNum);

    long getVacTotal(long userNum);

    long getVacLeft(long userNum);

    long getVacUsed(long userNum);

    void saveVacation(long vacUsed, long vacLeft, long userNum);

    void updateVacation(long requestSecond);

}
