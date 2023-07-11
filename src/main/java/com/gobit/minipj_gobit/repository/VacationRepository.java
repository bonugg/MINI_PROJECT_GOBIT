package com.gobit.minipj_gobit.repository;

import com.gobit.minipj_gobit.entity.Vacation;
import org.apache.commons.io.ByteOrderMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
    @Query(value = "SELECT * FROM t_vacation t WHERE t.usernum =:userNum", nativeQuery = true)
    Vacation findByUserNum(long userNum);
    @Query(value = "SELECT vac_total FROM t_vacation t WHERE t.usernum =:userNum", nativeQuery = true)
    long findVacTotalByUserNum(long userNum);

    @Query(value = "SELECT vac_left FROM t_vacation t WHERE t.usernum =:userNum", nativeQuery = true)
    long findVacLeftByUserNum(long userNum);

    @Query(value = "SELECT vac_used FROM t_vacation t WHERE t.usernum =:userNum", nativeQuery = true)
    long findVacUsedByUserNum(long userNum);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE t_vacation t SET t.vac_used = :vacUsed, t.vac_left = :vacLeft WHERE t.usernum = :userNum", nativeQuery = true)
    void request(@Param("vacUsed") long vacUsed, @Param("vacLeft") long vacLeft, @Param("userNum") long userNum);

//    @Query(value = "INSERT INTO t_vacation(vac_total, vac_sued, usernum, vac_left, vac_reqtmp) VALUES()")
//    void save(@Param())
}
