package com.gobit.minipj_gobit.repository;

import com.gobit.minipj_gobit.entity.Approval;
import com.gobit.minipj_gobit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    Optional<Approval> findByAppNum(long appNum);

    @Query(value ="SELECT app_vacreq FROM t_approval t WHERE t.app_num =:appNum", nativeQuery = true)
    long findAppVacReqByAppNum(long appNum);

    @Query(value = "select a from Approval a where a.userDept = :dept and a.appSort LIKE CONCAT('%', :cls, '%')")
    Page<Approval> findByDept(Pageable pageable, String dept, String cls);

    @Query(value = "select a from Approval a where a.userNum = :user and a.appSort LIKE CONCAT('%', :cls, '%')")
    Page<Approval> findByUser(Pageable pageable, User user, String cls);


    @Query(value = "select count(a.appAlarm) from Approval a where a.userNum = :user and (a.appState = '승인' or a.appState = '반려') and  a.appAlarm = 0")
    int findByCntUserApp(User user);

    void deleteByAppNum(long appNum);

    /*결재권자 표출*/
    @Query(value = "select count(*) from Approval a where a.userDept = :dept")
    int cntLeadTotalApp(String dept);
    @Query(value = "select count(*) from Approval a where a.appState = '미승인' and a.userDept = :dept")
    int cntLeadWaitApp(String dept);
    @Query(value = "select count(*) from Approval a where a.appState = '반려' and a.userDept = :dept")
    int cntLeadRejectApp(String dept);
    @Query(value = "select count(*) from Approval a where a.appState = '승인' and a.userDept = :dept")
    int cntLeadFinApp(String dept);

    Page<Approval> findByUserDeptAndAppContentContaining(Pageable pageble, String dept, String sWord);

    /*비결재권자(팀원) 표출*/
    @Query(value = "select count(*) from Approval a where a.userNum = :user")
    int cntMemTotalApp(User user);
    @Query(value = "select count(*) from Approval a where a.appState = '미승인' and a.userNum = :user")
    int cntMemWaitApp(User user);
    @Query(value = "select count(*) from Approval a where a.appState = '반려' and a.userNum = :user")
    int cntMemRejectApp(User user);
    @Query(value = "select count(*) from Approval a where a.appState = '승인' and a.userNum = :user")
    int cntMemFinApp(User user);

    Page<Approval> findByUserNumAndAppContentContaining(Pageable pageble, User user, String sWord);

    @Modifying(clearAutomatically = true)
    @Query(value="UPDATE t_approval t SET t.app_alarm =:i WHERE t.app_num =:appNum", nativeQuery = true)
    void updateAlarm(@Param("i") int i, @Param("appNum") long appNum);
}