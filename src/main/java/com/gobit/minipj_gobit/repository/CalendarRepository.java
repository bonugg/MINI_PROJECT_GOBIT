package com.gobit.minipj_gobit.repository;

import com.gobit.minipj_gobit.Entity.Calendar;
import com.gobit.minipj_gobit.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    @Query("SELECT C FROM Calendar C JOIN FETCH User U ON C.user = U WHERE U.USERDEPT =:DEPT ORDER BY C.CALNUM DESC")
    List<Calendar> findByCalList(@Param("DEPT") String DEPT);

    @Query(value = "SELECT * FROM T_CALENDAR T WHERE T.CAL_TYPE = '출퇴근' AND T.USER_NUM =:USERNUM AND T.CAL_START LIKE %:START%", nativeQuery = true)
    Optional<Calendar> findByUNandCS(long USERNUM, String START);
}
