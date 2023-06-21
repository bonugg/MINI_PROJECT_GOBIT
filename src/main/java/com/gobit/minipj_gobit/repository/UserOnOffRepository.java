package com.gobit.minipj_gobit.repository;

import com.gobit.minipj_gobit.Entity.UserOnOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOnOffRepository extends JpaRepository<UserOnOff, Long> {
    @Query(value = "SELECT * FROM t_commute T WHERE T.USER_NUM =:USERNUM AND T.GO_TO_WORK LIKE %:START%", nativeQuery = true)
    Optional<UserOnOff> findByUSERANDSTART(long USERNUM, String START);

    @Query(value = "SELECT TRUNCATE(AVG(COMMUTE_TIME), 1) FROM t_commute WHERE GET_OUT_WORK LIKE %:year% AND USER_NUM = :USERNUM GROUP BY USER_NUM", nativeQuery = true)
    double findByYearMonthChart(String year, long USERNUM);

    @Query(value = "SELECT T.GO_TO_WORK FROM t_commute T WHERE T.USER_NUM =:no AND T.GO_TO_WORK LIKE %:start%", nativeQuery = true)
    String findByCLASSIFYANDSTART(long no, String start);

    @Query(value = "SELECT T.GET_OUT_WORK FROM t_commute T WHERE T.USER_NUM =:no AND T.GET_OUT_WORK LIKE %:end%", nativeQuery = true)
    String findByCLASSIFYANDEND(long no, String end);
}
