package com.gobit.minipj_gobit.repository;

import com.gobit.minipj_gobit.entity.Testentity;
import com.gobit.minipj_gobit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Testentity, Long> {
    @Query(value = "SELECT COUNT(test_onoff) FROM t_test WHERE test_onoff = '승인' AND user_num =:usernum", nativeQuery = true)
    int findByCheckCnt(long usernum);

    Optional<Testentity> findByUser(User user);

}
