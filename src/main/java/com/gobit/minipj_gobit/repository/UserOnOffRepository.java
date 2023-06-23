package com.gobit.minipj_gobit.repository;

import com.gobit.minipj_gobit.Entity.UserOnOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public interface UserOnOffRepository extends JpaRepository<UserOnOff, Long>, DatabaseChangeRepository {
    @Query(value = "SELECT * FROM t_commute T WHERE T.USER_NUM =:USERNUM AND T.GO_TO_WORK LIKE %:START%", nativeQuery = true)
    Optional<UserOnOff> findByUSERANDSTART(long USERNUM, String START);

    @Query(value = "SELECT TRUNCATE(AVG(COMMUTE_TIME), 1) FROM t_commute WHERE GET_OUT_WORK LIKE %:year% AND USER_NUM = :USERNUM GROUP BY USER_NUM", nativeQuery = true)
    Double findByYearMonthChart(String year, long USERNUM);

    @Query(value = "SELECT T.GO_TO_WORK FROM t_commute T WHERE T.USER_NUM =:no AND T.GO_TO_WORK LIKE %:start%", nativeQuery = true)
    String findByCLASSIFYANDSTART(long no, String start);

    @Query(value = "SELECT T.GET_OUT_WORK FROM t_commute T WHERE T.USER_NUM =:no AND T.GET_OUT_WORK LIKE %:end%", nativeQuery = true)
    String findByCLASSIFYANDEND(long no, String end);

    Optional<UserOnOff> findByUPDATEAT(LocalDateTime UPDATEAT);

    @Query("SELECT MAX(UPDATEAT) FROM UserOnOff")
    LocalDateTime findMaxUpdatedAt();
    AtomicReference<LocalDateTime> previousUpdatedAt = new AtomicReference<>();
    @Override
    default Optional<LocalDateTime> checkForDatabaseChange() {
        LocalDateTime currentUpdatedAt = findMaxUpdatedAt();
        if (previousUpdatedAt.get() == null || (currentUpdatedAt != null && !currentUpdatedAt.isEqual(previousUpdatedAt.get()))) {
            previousUpdatedAt.set(currentUpdatedAt);
            System.out.println(currentUpdatedAt);
            return Optional.of(currentUpdatedAt);
        }
        return Optional.empty();
    }
}
